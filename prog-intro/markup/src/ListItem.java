package markup;

import java.util.List;

public class ListItem implements TexElement{
    private final List<ListElement> list;

    public ListItem(List<ListElement> list) {
        this.list = list;
    }

    public void toTex(StringBuilder stringBuilder) {
        stringBuilder.append("\\item ");
        for (ListElement listElement : list) {
            listElement.toTex(stringBuilder);
        }
    }
}

