package markup;

import java.util.List;

public class Emphasis extends AbstractSelection {
    public Emphasis(List<ParagraphElement> list) {
        super(list);
    }

    @Override
    protected String markdownSymbol() {
        return "*";
    }

    @Override
    protected String texSymbol() {
        return "emph";
    }
}
