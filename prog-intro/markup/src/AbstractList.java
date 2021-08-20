package markup;

import java.util.List;

public abstract class AbstractList implements ListElement{
    private final List<ListItem> list;

    protected AbstractList(List<ListItem> list) {
        this.list = list;
    }

    @Override
    public void toTex(StringBuilder stringBuilder) {
        stringBuilder.append("\\begin{").append(texSymbol()).append("}");
        for (ListItem listItem : list) {
            listItem.toTex(stringBuilder);
        }
        stringBuilder.append("\\end{").append(texSymbol()).append("}");
    }

    protected abstract String texSymbol();
}
