package application.gui;

public class MainPaneManager
{
    private static ControllerMainPain c;

    public static void setC(ControllerMainPain cc)
    {
        c = cc;
    }

    public static ControllerMainPain getC()
    {
        return c;
    }
}
