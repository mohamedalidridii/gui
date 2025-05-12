import pandas as pd
import numpy as np
import joblib
import pickle
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report, accuracy_score
from sklearn.model_selection import GridSearchCV
from sklearn.preprocessing import StandardScaler
np.random.seed(0)


df=pd.read_csv("Python/highly_repetitive_travel_destinations.csv")
def clean_destination (destination):
    return destination.split("Variant")[0].strip()

df["Destination"]=df["Destination"].apply(clean_destination)
print("before",df.head())
label_encoders={}
categorial_columns=["Mood","Destination","Family Friendly"]

for col in categorial_columns:
    le =LabelEncoder()
    df[col]=le.fit_transform(df[col])
    label_encoders[col]=le

df[["temp_min","temp_max"]]=df["Temperature (°C)"].str.replace('°C', '', regex=False).str.extract(r'(\d+)[–-](\d+)').astype(int)
df["temp_avg"]=df[["temp_min","temp_max"]].mean(axis=1)

df[['duration_min', 'duration_max']] = df['Duration Range (days)'].str.replace('days', '', regex=False).str.extract(r'(\d+)[–-](\d+)').astype(int)
df['duration_avg'] = df[['duration_min', 'duration_max']].mean(axis=1)
df.drop(["Temperature (°C)","Duration Range (days)"],axis=1,inplace=True)
df.drop(['duration_min', 'duration_max',"temp_min","temp_max","Activities"],axis=1,inplace=True)
numeric_cols=["temp_avg","duration_avg"]
scaler=StandardScaler()
df[numeric_cols]=scaler.fit_transform(df[numeric_cols])

df = df.rename(columns={"Physicality (1–5)": "Physicality"})
df = df.rename(columns={"Security (1–5)": "Security"})




le_target = LabelEncoder()
y = df["Destination"].values
x=df.drop("Destination",axis=1)
print("Training feature columns:", x.columns.tolist())

x_train, x_test, y_train,y_test=train_test_split(x,y,test_size=0.2,random_state=42)

rf=RandomForestClassifier(n_jobs=-1,random_state=42,class_weight='balanced')
rf.fit(x_train,y_train)
y_pred=rf.predict(x_test)

print("after",df.head())


print("Accuracy:", accuracy_score(y_test, y_pred))
print("X shape:", x.shape)
print(label_encoders["Mood"].classes_)

with open("Python/rf_model.pkl","wb") as f :
    joblib.dump(rf,f )
with open("Python/feature_order.pkl","wb") as f :
    joblib.dump(x.columns.tolist(), f)
with open("Python/label_encoders.pkl","wb") as f :
    joblib.dump(label_encoders,f )
with open( "Python/scaler.pkl","wb") as f :
    joblib.dump(scaler,f)









