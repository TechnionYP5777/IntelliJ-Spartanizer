package il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiStatement;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.KeyDescriptionParameters;

/**
 * @author michalcohen
 * @since 11-01-17
 */
public class GenericPsiStatement extends GenericPsi implements PsiStatement {

    public GenericPsiStatement(PsiElement inner) {
        super(inner, "generic statement");
    }

    @Override
    public String toString() {
        return "Generic statement" + inner.getUserData(KeyDescriptionParameters.ID);
    }

    @Override
    public boolean generalizes(PsiElement e) {
        return iz.statement(e) && !iz.blockStatement(e);
    }

    /*@Override
    public GenericPsiStatement copy() {
        return new GenericPsiStatement(inner.copy());
    }
    */
}