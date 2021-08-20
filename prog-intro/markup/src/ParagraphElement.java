package markup;

public interface ParagraphElement extends Element {
    @Override
    void toMarkdown(StringBuilder stringBuilder);
    @Override
    void toTex(StringBuilder stringBuilder);
}
