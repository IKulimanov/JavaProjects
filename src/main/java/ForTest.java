public class ForTest implements interTest {
   public String strTest;
   public int numTest;
   private void funcTestOne(int k)
    {
        k++;
    }

    public String funcTestTwo(String st) {
        st = "privet Duck";
        System.out.println(st);
        return st;
    }

    public void funcTestThree() { System.out.println("poka Duck"); }

}
class ForTestTwo extends ForTest{
    public final String privet = "DRUG";
    private int number = 1;
}
interface interTest extends interMore {
    String funcTestTwo(String st);
}
interface interMore {
    void funcTestThree();
}