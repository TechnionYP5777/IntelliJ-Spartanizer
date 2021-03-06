package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.List;
import java.util.Map;

/**
 * A basic block representing an expression ("expression(2)").
 *
 * @author Oren Afek
 * @since 5/3/2017.
 */
public class Expression extends GenericMethodCallBasedBlock {

    private static final String TEMPLATE = "expression";
    PsiType type;

    public Expression(PsiElement e, PsiType type) {
        super(e, TEMPLATE);
        this.type = type;
    }

    protected Expression(Encapsulator n, String temp, PsiType t) {
        super(n, temp);
        type = t;
    }

    protected Expression(String template) {
        super(template);
    }

    public Expression(Encapsulator n) {
        super(n, TEMPLATE);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected Expression() {
        super(TEMPLATE);
    }

    @Override
    public MatchingResult generalizes(Encapsulator e, Map<Integer, List<PsiElement>> m) {
        return new MatchingResult(super.generalizes(e, m).matches() && iz.expression(e.getInner()));
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return next != null && prev.getText().equals(next.getText());
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        return new Expression(e);
    }

    public PsiType evaluationType() {
        return type;
    }

    /* Constraints */

    public void mustBeArithmetic() {
        addConstraint((e, m) -> iz.expression(e.inner) && iz.arithmetic(az.expression(e.inner)));
    }

    public void mustNotRefer(Integer id) {
        addConstraint((e, m) -> countReferences(e, id, m) == 0);
    }

    private int countReferences(Encapsulator e, Integer id, Map<Integer, List<PsiElement>> m) {
        Wrapper<Integer> wi = new Wrapper<>(0);
        e.accept(n -> {
            if (iz.identifier(n.getInner()) && az.identifier(n.getInner()).getText().equals(m.get(id).get(0).getText()))
                wi.set(wi.get() + 1);
        });
        return wi.get();
    }
}
