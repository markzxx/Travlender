调用建议：
    目前仅支持调用活动Search_Location，其他活动可以忽略。也就是只能调用活动（onCreate）。
    暂时没有外部可调用的方法。

关于安装包：
    首先尝试运行，如果Search_Location.java报错，执行以下操作。
    在WHL文件夹下有一些关于地图的安装包，将其复制粘贴到app/libs下即可。
    之后运行如果Search_Location.java没有报错（说明关于地图的API已经成功import），之后系统可能会报错大致如下：
        ...
        ...DuplicateFileException:Duplicate files copied in APK assets/ap1.data
        File1: ...
        File2: ...
        ...
    此时只需再把app/libs下刚才粘贴的关于地图的安装包删掉即可正常运行，无需再考虑这个问题。

文件说明
    Activity:
        Search_Location
            搜索地点的主界面，搜索结果按距离排序。但是暂时不显示具体距离，这一点有待完善。（不要问我为什么，我正在解决）
        OpenMap
            在地图上显示当前坐标。目前这个界面还没有做完。暂且用它来看看自己的定位功能是否正常开启。

    Layout:
        search_Location
            对应活动Search_Location，显示搜索地点的主界面。
        openmap
            对应活动OpenMap，显示搜索地点的主界面。
        point_of_interest
            列表中单条记录的格式。

    内部类（无需调用，不对外提供方法）：
        Point_of_Interest_Adapter
            用于搜索地点的主界面的列表
        Point_of_Interest
            用于搜索地点的主界面的列表
