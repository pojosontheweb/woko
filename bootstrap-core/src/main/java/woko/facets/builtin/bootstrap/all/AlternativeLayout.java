package woko.facets.builtin.bootstrap.all;

public enum AlternativeLayout {

    fluid(
            "http://twitter.github.com/bootstrap/assets/img/examples/bootstrap-example-fluid.png",
            "Create a fluid, two-column page. Great for applications and docs."
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