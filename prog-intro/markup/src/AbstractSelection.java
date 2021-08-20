package markup;

import java.util.List;

public abstract class AbstractSelection implements ParagraphElement {
    private final List<ParagraphElement> list;

    protected AbstractSelection(List<ParagraphElement> list) {
        this.list = list;
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        stringBuilder.append(markdownSymbol());
        for (ParagraphElement paragraphElement : list) {
            paragraphElement.toMarkdown(stringBuilder);
        }
        stringBuilder.append(markdownSymbol());
    }

    @Override
    public void toTex(StringBuilder stringBuilder) {
        stringBuilder.append("\\").append(texSymbol()).append("{");
        for (ParagraphElement paragraphElement : list) {
            paragraphElement.toTex(stringBuilder);
        }
        stringBuilder.append("}");
    }

    protected abstract String markdownSymbol();
    protected abstract String texSymbol();
}
