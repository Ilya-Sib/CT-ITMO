package markup;

import java.util.List;

public class Strikeout extends AbstractSelection {
    public Strikeout(List<ParagraphElement> list) {
        super(list);
    }

    @Override
    protected String markdownSymbol() {
        return "~";
    }

    @Override
    protected String texSymbol() {
        return "textst";
    }
}
