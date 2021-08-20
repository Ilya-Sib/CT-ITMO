package markup;

import java.util.List;

public class Strong extends AbstractSelection {
    public Strong(List<ParagraphElement> list) {
        super(list);
    }

    @Override
    protected String markdownSymbol() {
        return "__";
    }

    @Override
    protected String texSymbol() {
        return "textbf";
    }
}
