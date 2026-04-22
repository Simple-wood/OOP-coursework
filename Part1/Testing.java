/**
 * Test harness for validating core behaviors of the Typist class.
 *
 * Contains focused console-based tests for progress updates, accuracy clamping,
 * burnout recovery, reset behavior, and slide-back logic.
 * Documentation and explanations of each test can be found in the report!
 *
 * @author Kishal Chhetri
 * @version 1.0
 */
public class Testing {
    /**
     * Tests that calling typeCharacter() advances progress correctly.
     * Includes a zero-iteration loop case to confirm no unintended movement.
     */
    public static void testTypeCharacter()
    {
        Typist typer = new Typist('*', "Kishal", 0.5);
        for(int x = 0; x < 5; x++) 
        {
            typer.typeCharacter();
            System.out.println("Progress is now at " + typer.getProgress());
        }

        System.out.println("Progress is now at " + typer.getProgress());

        Typist typer2 = new Typist('!', "Raayan", 0.85);

        for(int x = 0; x < 0; x++) 
        {
            typer2.typeCharacter();
            System.out.println("Progress is now at " + typer2.getProgress());
        }

        System.out.println();
        System.out.println("Progress is now at " + typer2.getProgress());
        System.out.println();
    }

    /**
     * Tests setAccuracy() with in-range and out-of-range values.
     * Verifies values are clamped to the valid range [0.0, 1.0].
     */
    public static void testSetAccuracy()
    {
        Typist typer = new Typist('*', "Kishal", 0.5);

        typer.setAccuracy(0.34);    
        System.out.println("Typist accuracy is - " + typer.getAccuracy());

        typer.setAccuracy(1.0);
        System.out.println("Typist accuracy is - " + typer.getAccuracy());

        typer.setAccuracy(0.0);
        System.out.println("Typist accuracy is - " + typer.getAccuracy());

        typer.setAccuracy(1.01); 
        System.out.println("Typist accuracy is - " + typer.getAccuracy());

        typer.setAccuracy(-1.69);
        System.out.println("Typist accuracy is - " + typer.getAccuracy());

        System.out.println();
    }

    /**
     * Tests resetToStart() after simulated typing and burnout.
     * Confirms progress and burnout state are fully reset.
     */
    public static void testResetToStart()
    {
        Typist typer = new Typist('*', "Kishal", 0.5);


        for(int x = 0; x < 10; x++)
        {
            typer.typeCharacter();
        }

        typer.burnOut(10);
       
        System.out.println("Before");
        System.out.println("Progress - " + typer.getProgress());
        System.out.println("Burnt out turns - " + typer.getBurnoutTurnsRemaining());
        System.out.println("Is typer burn out? - " + typer.isBurntOut());

        typer.resetToStart();

        System.out.println("After");
        System.out.println("Progress - " + typer.getProgress());
        System.out.println("Burnt out turns - " + typer.getBurnoutTurnsRemaining());
        System.out.println("Is typer burn out? - " + typer.isBurntOut());

        System.out.println();
    }

    /**
     * Tests recoverFromBurnout() with positive, zero, and negative burnout inputs.
     * Ensures burnout countdown and recovery behavior are handled safely.
     */
    public static void testRecoverFromBurnout()
    {
        Typist typer = new Typist('*', "Kishal", 0.5);
       
        typer.burnOut(5);
        while(typer.isBurntOut()){
            System.out.println(typer.getBurnoutTurnsRemaining());
            typer.recoverFromBurnout();
        }

        System.out.println(typer.getBurnoutTurnsRemaining());
        System.out.println();

        typer.burnOut(0);
        while(typer.isBurntOut()){
            System.out.println(typer.getBurnoutTurnsRemaining());
            typer.recoverFromBurnout();
        }

        System.out.println(typer.getBurnoutTurnsRemaining());
        System.out.println();

        typer.burnOut(-1);
        while(typer.isBurntOut()){
            System.out.println(typer.getBurnoutTurnsRemaining());
            typer.recoverFromBurnout();
        }

        System.out.println(typer.getBurnoutTurnsRemaining());
        System.out.println();
    }

    /**
     * Tests slideBack() with normal and excessive amounts.
     * Confirms progress never drops below zero.
     */
    public static void testSlideBack()
    {
        Typist typer = new Typist('*', "Kishal", 0.5);
       
        for(int i = 0; i < 10; i++)
        {
            typer.typeCharacter();
        }


        System.out.println(typer.getProgress());
        typer.slideBack(5);
        System.out.println(typer.getProgress());
        System.out.println();

        Typist typer2 = new Typist('*', "Kishal", 0.5);
       
        for(int i = 0; i < 10; i++)
        {
            typer2.typeCharacter();
        }


        System.out.println(typer2.getProgress());
        typer2.slideBack(10);
        System.out.println(typer2.getProgress());
        System.out.println();

        Typist typer3 = new Typist('*', "Kishal", 0.5);
       
        for(int i = 0; i < 10; i++)
        {
            typer3.typeCharacter();
        }

        System.out.println(typer3.getProgress());
        typer3.slideBack(15);
        System.out.println(typer3.getProgress());
        System.out.println();
    }

    public static void main(String[] args)
    {
        System.out.println("Testing typeCharacter() - ");
        testTypeCharacter();

        System.out.println("Testing setAccuracy() - ");
        testSetAccuracy();

        System.out.println("Testing resetToStart() - ");
        testResetToStart();

        System.out.println("Testing recoverFromBurnout() - ");
        testRecoverFromBurnout();

        System.out.println("Testing slideBack() - ");
        testSlideBack();
    }

}
