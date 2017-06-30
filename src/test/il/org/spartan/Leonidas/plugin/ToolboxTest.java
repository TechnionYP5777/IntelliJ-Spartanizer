package il.org.spartan.Leonidas.plugin;

import com.intellij.psi.PsiFile;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Amir Sagiv
 * @since 03-05-2017
 */
public class ToolboxTest extends PsiTypeHelper {

    private Toolbox tb;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        tb = new Toolbox();
        tb.initComponent();
    }

    @Override
    protected void tearDown() throws Exception {
        clearFields(this);
        super.tearDown();
    }


    public void testGetAllTippers() throws Exception {
        List<Tipper> list = tb.getAllTippers();
        List<String> names = new ArrayList<>();
        list.forEach(tipper -> names.add(tipper.name()));

        assertTrue(names.contains("SafeReference"));
        assertTrue(names.contains("Unless"));
        assertTrue(names.contains("RemoveCurlyBracesFromIfStatement"));
        assertTrue(names.contains("IfEmptyThen"));
        assertTrue(names.contains("LispLastElement"));
        assertTrue(names.contains("DefaultsTo"));
        assertTrue(names.contains("RemoveCurlyBracesFromWhileStatement"));
        assertTrue(names.contains("LambdaExpressionRemoveRedundantCurlyBraces"));
        assertTrue(names.contains("DoubleNot"));
    }

    public void testGetCurrentTippers() throws Exception {
        List<String> currNames = tb.getAllTippers().stream().map(Tipper::name).collect(Collectors.toList());

        assertFalse(currNames.contains("myOpt"));
        assertTrue(currNames.contains("SafeReference"));
        assertTrue(currNames.contains("Unless"));
        assertTrue(currNames.contains("RemoveCurlyBracesFromIfStatement"));
        assertTrue(currNames.contains("IfEmptyThen"));
        assertTrue(currNames.contains("LispLastElement"));
        assertTrue(currNames.contains("DefaultsTo"));
        assertTrue(currNames.contains("RemoveCurlyBracesFromWhileStatement"));
        assertTrue(currNames.contains("LambdaExpressionRemoveRedundantCurlyBraces"));
        assertTrue(currNames.contains("DoubleNot"));

    }

    public void testUpdateTipperList() throws Exception {
        List<Tipper> list = tb.getAllTippers();
        List<String> names = new ArrayList<>();
        list.forEach(tipper -> names.add(tipper.name()));

        names.remove("SafeReference");
        names.remove("Unless");
        tb.updateTipperList(names);
        list = tb.getCurrentTippers();
        List<String> currNames = new ArrayList<>();
        list.forEach(tipper -> currNames.add(tipper.name()));


        assertFalse(currNames.contains("SafeReference"));
        assertFalse(currNames.contains("Unless"));
        assertTrue(currNames.contains("RemoveCurlyBracesFromIfStatement"));
        assertTrue(currNames.contains("IfEmptyThen"));

        names.remove("SafeReference");
        names.add("Unless");
        tb.updateTipperList(names);
        list = tb.getCurrentTippers();
        List<String> finalNames = new ArrayList<>();
        list.forEach(tipper -> finalNames.add(tipper.name()));

        assertFalse(finalNames.contains("SafeReference"));
        assertTrue(finalNames.contains("Unless"));
        assertTrue(finalNames.contains("RemoveCurlyBracesFromIfStatement"));
        assertTrue(finalNames.contains("IfEmptyThen"));
    }

    public void testIsElementOfOperableType() throws Exception {
        assertTrue(tb.isElementOfOperableType(createTestStatementFromString("if(true == false){}")));
        //Removing because it will fail in the future. but works fine.
        //assertFalse(tb.isElementOfOperableType(createTestStatementFromString("for(;;){}")));
        assertTrue(tb.isElementOfOperableType(createTestStatementFromString("while(true){}")));
    }

    public void testCanTip() throws Exception {
        assertTrue(tb.canTip(createTestIfStatement("true", "x++;")));
        assertTrue(tb.canTip(createTestWhileStatementFromString("while(true){x++;}")));
        //Removing because it will fail in the future. but works fine.
        //assertFalse(tb.canTip(createTestIfStatement("true", "x++;\ny++;")));
    }

    public void testGetTipper() throws Exception {
        assertEquals(tb.getTipper(createTestStatementFromString("if(true){x++;}")).name(), "RemoveCurlyBracesFromIfStatement");
        assertEquals(tb.getTipper(createTestStatementFromString("while(true){x++;}")).name(), "RemoveCurlyBracesFromWhileStatement");
        assertEquals(tb.getTipper(createTestExpressionFromString("x += 1")).name(), "ReplaceIdPlusOneWithIdPlusPlus");
    }

    public void testCheckExcluded() throws Exception {
        PsiFile f = createTestFileFromString("class A{}");
        tb.excludeFile(f);
        assertTrue(tb.checkExcluded(f));
        tb.includeFile(f);
        assertFalse(tb.checkExcluded(f));
    }


}