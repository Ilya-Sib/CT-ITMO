package markup;

import java.util.List;

public class Paragraph implements MarkdownElement, ListElement{
    private final List<ParagraphElement> list;

    public Paragraph(List<ParagraphElement> list) {
        this.list = list;
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        for (ParagraphElement paragraphElement : list) {
            paragraphElement.toMarkdown(stringBuilder);
        }
    }

    @Override
    public void toTex(StringBuilder stringBuilder) {
        for (ParagraphElement paragraphElement : list) {
            paragraphElement.toTex(stringBuilder);
        }
    }
}
