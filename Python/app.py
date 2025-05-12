from flask import Flask, request, jsonify
import pandas as pd
import joblib

# Load saved model and preprocessing tools
model = joblib.load("Python/rf_model.pkl")
label_encoders = joblib.load("Python/label_encoders.pkl")
scaler = joblib.load("Python/scaler.pkl")
feature_order = joblib.load("Python/feature_order.pkl")


app = Flask(__name__)

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.get_json()
        if not data:
            return jsonify({'error': 'No input data provided'}), 400

        # Convert JSON to DataFrame
        input_df = pd.DataFrame([data])

        # Apply label encoders to categorical columns
        for col, le in label_encoders.items():
            if col == "Destination":
                continue  # Skip encoding Destination at prediction time
            if col not in input_df:
                return jsonify({'error': f'Missing field: {col}'}), 400
            input_df[col] = le.transform(input_df[col])

        # Apply scaler to numerical columns
        if "temp_avg" not in input_df or "duration_avg" not in input_df:
            return jsonify({'error': 'Missing temp_avg or duration_avg fields'}), 400

        input_df[["temp_avg", "duration_avg"]] = scaler.transform(input_df[["temp_avg", "duration_avg"]])

        # Ensure no extra columns like 'Destination' in input
        if 'Destination' in input_df.columns:
            input_df = input_df.drop(columns=['Destination'])
        input_df = input_df[feature_order]
        pred = model.predict(input_df)[0]

        # Decode prediction using the reverse label encoder for Destination
        destination_le = label_encoders["Destination"]
        predicted_destination = destination_le.inverse_transform([pred])[0]

        return jsonify({'recommended_destination': predicted_destination})
        print("Received columns:", input_df.columns.tolist())

    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, port=5000)
