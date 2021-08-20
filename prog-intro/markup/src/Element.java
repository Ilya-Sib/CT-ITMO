package markup;

public interface Element extends MarkdownElement, TexElement {
    @Override
    void toMarkdown(StringBuilder stringBuilder);

    @Override
    void toTex(StringBuilder stringBuilder);
}
