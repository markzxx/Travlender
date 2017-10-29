package cs309.travlender.WHL;

/**
 * Created by Dell on 2017/10/28.
 */

class Point_of_Interest {
    private String name;
    private String subname;
    private int imageId;
    private int distance;

    public Point_of_Interest(String name, int imageId, String subname, int distance){
        this.imageId = imageId;
        this.name = name;
        this.subname = subname;
        this.distance = distance;
    }

    public String getName(){
        return name;
    }

    public String getSubname() { return subname; }

    public int getImageId(){
        return imageId;
    }

    public String getDistance() {
        if (distance>1000){
            return String.format("%.1fkm", (float)distance/1000);
        }
        else
            return String.format("%dkm", distance);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
