package application.models;

import java.util.ArrayList;

public class EmployeeConditionsManager
{
    private ImportantD importantD;
    private EmployeeD employeeD;

    private RelationD relationD;
    private ConditionsD conditionsD;
    private NextConditionsD nextConditionsD;
    private ArrayList<WageD> wageDS;

    private PositionD positionD;
    private PlaceD placeD;
    private LevelD levelD;
    private MinimumWageD minimumWageD;

    private ArrayList<YearD> yearDS;
    private ArrayList<MonthD> monthDS;

    public EmployeeConditionsManager() {
        wageDS = new ArrayList<>();
        yearDS = new ArrayList<>();
        monthDS = new ArrayList<>();
    }

    public ImportantD getImportantD() {
        return importantD;
    }

    public void setImportantD(ImportantD importantD) {
        this.importantD = importantD;
    }

    public EmployeeD getEmployeeD() {
        return employeeD;
    }

    public void setEmployeeD(EmployeeD employeeD) {
        this.employeeD = employeeD;
    }

    public RelationD getRelationD() {
        return relationD;
    }

    public void setRelationD(RelationD relationD) {
        this.relationD = relationD;
    }

    public ConditionsD getConditionsD() {
        return conditionsD;
    }

    public void setConditionsD(ConditionsD conditionsD) {
        this.conditionsD = conditionsD;
    }

    public NextConditionsD getNextConditionsD() {
        return nextConditionsD;
    }

    public void setNextConditionsD(NextConditionsD nextConditionsD) {
        this.nextConditionsD = nextConditionsD;
    }

    public ArrayList<WageD> getWageDS() {
        return wageDS;
    }

    public void setWageDS(ArrayList<WageD> wageDS) {
        this.wageDS = wageDS;
    }

    public PositionD getPositionD() {
        return positionD;
    }

    public void setPositionD(PositionD positionD) {
        this.positionD = positionD;
    }

    public PlaceD getPlaceD() {
        return placeD;
    }

    public void setPlaceD(PlaceD placeD) {
        this.placeD = placeD;
    }

    public LevelD getLevelD() {
        return levelD;
    }

    public void setLevelD(LevelD levelD) {
        this.levelD = levelD;
    }

    public MinimumWageD getMinimumWageD() {
        return minimumWageD;
    }

    public void setMinimumWageD(MinimumWageD minimumWageD) {
        this.minimumWageD = minimumWageD;
    }

    public ArrayList<YearD> getYearDS() {
        return yearDS;
    }

    public void setYearDS(ArrayList<YearD> yearDS) {
        this.yearDS = yearDS;
    }

    public ArrayList<MonthD> getMonthDS() {
        return monthDS;
    }

    public void setMonthDS(ArrayList<MonthD> monthDS) {
        this.monthDS = monthDS;
    }
}
