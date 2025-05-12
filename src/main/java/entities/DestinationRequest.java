package entities;

public class DestinationRequest {
    private String mood;
    private int physicality;
    private String familyFriendly;
    private int budgetPerDay;
    private int security;
    private double tempAvg;
    private int durationAvg;



    public DestinationRequest(String mood, int physicality, String familyFriendly,
                         int budgetPerDay, int security, double tempAvg, int durationAvg) {
        this.mood = mood;
        this.physicality = physicality;
        this.familyFriendly = familyFriendly;
        this.budgetPerDay = budgetPerDay;
        this.security = security;
        this.tempAvg = tempAvg;
        this.durationAvg = durationAvg;
    }

    public String getMood() { return mood; }
    public int getPhysicality() { return physicality; }
    public String getFamilyFriendly() { return familyFriendly; }
    public int getBudgetPerDay() { return budgetPerDay; }
    public int getSecurity() { return security; }
    public double getTempAvg() { return tempAvg; }
    public int getDurationAvg() { return durationAvg; }

}
