package application.core;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Levies
{
    Map<String,BigDecimal[]> SD1A;
    Map<String,BigDecimal[]> SD2B;
    Map<String,BigDecimal[]> SD2A;

    Map<String,BigDecimal[]> ID1A;
    Map<String,BigDecimal[]> ID1B;
    Map<String,BigDecimal[]> ID2A;
    Map<String,BigDecimal[]> ID2B;

    Map<String,BigDecimal[]> PD1A;
    Map<String,BigDecimal[]> PD2C;
    Map<String,BigDecimal[]> PD2B;
    Map<String,BigDecimal[]> PD2A;

    Map<String,BigDecimal[]> NZ1A;
    Map<String,BigDecimal[]> NZ2C2;
    Map<String,BigDecimal[]> NZ2C1;
    Map<String,BigDecimal[]> NZ2A;
    Map<String,BigDecimal[]> NZ2B;

    public Levies() {
        setSD1A();
        setSD2B();
        setSD2A();

        setID1A();
        setID1B();
        setID2A();
        setID2B();

        setPD1A();
        setPD2C();
        setPD2B();
        setPD2A();

        setNZ1A();
        setNZ2C2();
        setNZ2C1();
        setNZ2A();
        setNZ2B();
    }

    private void setNZ1A()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0.014"), new BigDecimal("0.014"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.14"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0.03"), new BigDecimal("0.03"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0.01"), new BigDecimal("0.01"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0475"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.1"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        NZ1A=hm;
    }

    private void setNZ2C1()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        NZ2C1=hm;
    }

    private void setNZ2C2()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.14"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0.03"), new BigDecimal("0.03"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0475"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        NZ2C2=hm;
    }

    private void setNZ2A()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0.014"), new BigDecimal("0.014"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.14"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0.03"), new BigDecimal("0.03"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0.01"), new BigDecimal("0.01"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0475"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.1"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        NZ2A=hm;
    }

    private void setNZ2B()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.14"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0.03"), new BigDecimal("0.03"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0475"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.1"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        NZ2B=hm;
    }

    private void setSD1A()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0.014"), new BigDecimal("0.014"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.14"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0475"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.1"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        SD1A=hm;
    }

    private void setSD2A()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        SD2A=hm;
    }

    private void setSD2B()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.14"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0475"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        SD2B=hm;
    }

    private void setID1A()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0.014"), new BigDecimal("0.014"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.14"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0.03"), new BigDecimal("0.03"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0.01"), new BigDecimal("0.01"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0475"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0.02"), new BigDecimal("0.05"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        ID1A=hm;
    }

    private void setID1B()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0.014"), new BigDecimal("0.014"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.14"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0.03"), new BigDecimal("0.03"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0475"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0.02"), new BigDecimal("0.05"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        ID1B=hm;
    }

    private void setID2A()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        ID2A=hm;
    }

    private void setID2B()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.14"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0.03"), new BigDecimal("0.03"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0475"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        ID2B=hm;
    }

    private void setPD2A()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        PD2A=hm;
    }

    private void setPD2B()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.14"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0475"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        PD2B=hm;
    }

    private void setPD2C()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.14"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0475"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.10"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        PD2C=hm;
    }

    private void setPD1A()
    {
        Map<String,BigDecimal[]> hm = new HashMap< String,BigDecimal[]>();
        BigDecimal arr[];
        arr = new BigDecimal[] {new BigDecimal("0.014"), new BigDecimal("0.014"), new BigDecimal("1")};
        hm.put("nemocenske", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.14"), new BigDecimal("1")};
        hm.put("starobne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("invalidne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")};
        hm.put("nezamestnanost", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0025"), new BigDecimal("1")};
        hm.put("garancne", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.008"), new BigDecimal("0")};
        hm.put("urazove", arr);
        arr = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("0.0475"), new BigDecimal("1")};
        hm.put("solidarita", arr);
        arr = new BigDecimal[] {new BigDecimal("0.04"), new BigDecimal("0.10"), new BigDecimal("0")};
        hm.put("zdravotne", arr);
        PD1A=hm;
    }

    public Map<String, BigDecimal[]> getSD1A() {
        return SD1A;
    }

    public Map<String, BigDecimal[]> getSD2B() {
        return SD2B;
    }

    public Map<String, BigDecimal[]> getSD2A() {
        return SD2A;
    }

    public Map<String, BigDecimal[]> getID1A() {
        return ID1A;
    }

    public Map<String, BigDecimal[]> getID1B() {
        return ID1B;
    }

    public Map<String, BigDecimal[]> getID2A() {
        return ID2A;
    }

    public Map<String, BigDecimal[]> getID2B() {
        return ID2B;
    }

    public Map<String, BigDecimal[]> getPD1A() {
        return PD1A;
    }

    public Map<String, BigDecimal[]> getPD2C() {
        return PD2C;
    }

    public Map<String, BigDecimal[]> getPD2B() {
        return PD2B;
    }

    public Map<String, BigDecimal[]> getPD2A() {
        return PD2A;
    }

    public Map<String, BigDecimal[]> getNZ1A() {
        return NZ1A;
    }

    public Map<String, BigDecimal[]> getNZ2C2() {
        return NZ2C2;
    }

    public Map<String, BigDecimal[]> getNZ2C1() {
        return NZ2C1;
    }

    public Map<String, BigDecimal[]> getNZ2A() {
        return NZ2A;
    }

    public Map<String, BigDecimal[]> getNZ2B() {
        return NZ2B;
    }
}
