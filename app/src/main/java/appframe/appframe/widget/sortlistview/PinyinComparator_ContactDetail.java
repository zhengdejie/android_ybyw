package appframe.appframe.widget.sortlistview;

import java.util.Comparator;

import appframe.appframe.dto.ContactDetail;

/**
 * Created by Administrator on 2015/11/17.
 */
public class PinyinComparator_ContactDetail implements Comparator<ContactDetail> {

    public int compare(ContactDetail o1, ContactDetail o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
