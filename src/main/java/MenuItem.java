public class MenuItem {
    public String visibleName;
    public String firstTag;
    public String secondTag;

    public String getVisibleName() {
        return visibleName;
    }

    public void setVisibleName(String visibleName) {
        this.visibleName = visibleName;
    }

    public String getFirstTag() {
        return firstTag;
    }

    public void setFirstTag(String firstTag) {
        this.firstTag = firstTag;
    }

    public String getSecondTag() {
        return secondTag;
    }

    public void setSecondTag(String secondTag) {
        this.secondTag = secondTag;
    }

    MenuItem(String visibleName, String firstTag, String  secondTag){
        this.visibleName = visibleName;
        this.firstTag = firstTag;
        this.secondTag = secondTag;
    }
}
