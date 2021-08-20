package markup;

public interface ListElement extends TexElement {
    @Override
    void toTex(StringBuilder stringBuilder);
}
