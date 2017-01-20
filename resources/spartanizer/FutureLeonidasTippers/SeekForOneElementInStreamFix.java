package spartanizer.FutureLeonidasTippers;

import com.intellij.psi.PsiArrayAccessExpression;
import com.intellij.psi.PsiMethodCallExpression;
import il.org.spartan.ispartanizer.plugin.leonidas.GenericPsiElementStub;
import il.org.spartan.ispartanizer.plugin.leonidas.Leonidas;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Replace arr[index]; index++ with arr[index++]
 *
 * @author Oren Afek
 * @since 10/01/17
 */
public class SeekForOneElementInStreamFix extends GenericPsiElementStub {

    @Leonidas(PsiMethodCallExpression.class)
    public void from(){
        List<Object> l = Arrays.stream(arrayIdentifier(0))
                .streamMethodInvocations(1)
                .collect(Collectors.toList());
                return !l.isEmpty() ? l.get(0) : null;
    }

    @Leonidas(PsiMethodCallExpression.class)
    public void to(){
        return Arrays.stream(arrayIdentifier(0))
                .streamMethodInvocations(1)
                .findAny().orElseGet(null);
    }
}