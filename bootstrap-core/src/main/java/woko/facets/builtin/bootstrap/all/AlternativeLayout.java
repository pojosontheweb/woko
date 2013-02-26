package woko.facets.builtin.bootstrap.all;

public enum AlternativeLayout {

    fluid(
            "http://farm9.staticflickr.com/8509/8510298090_630b11f7e6_n.jpg",
            "Create a fluid, two-column page. Great for applications and docs."
    ),
    absolut(
            "http://farm9.staticflickr.com/8366/8509189293_be3fc81185_n.jpg",
            "Logo, title, slogan and searchBar are separated from the navBar."
    );

    private String imgUrl;
    private String description;

    private AlternativeLayout(String imgUrl, String description) {
        this.imgUrl = imgUrl;
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}