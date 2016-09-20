function getGarde(gardeCode) {
    var grade = "保密";
    switch(gardeCode){
        case 0:	grade = "茅屋";break;
        case 1:	grade = "蜗居";break;
        case 2:	grade = "公寓";break;
        case 3:	grade = "洋房";break;
        case 4:	grade = "别墅";break;
        case 5:	grade = "庄园";break;
        case 6:	grade = "城堡";break;
        case 7:	grade = "岛屿";break;
        case 8:	grade = "海洋";break;
        case 9:	grade = "国度";break;
        case 10:grade = "水星";break;
        case 11:grade = "金星";break;
        case 12:grade = "地球";break;
        case 13:grade = "火星";break;
        case 14:grade = "木星";break;
        case 15:grade = "土星";break;
        case 16:grade = "天王星";break;
        case 17:grade = "海王星";break;
        case 18:grade = "冥王星";break;
        case 19:grade = "太阳系";break;
        case 20:grade = "银河系";break;
    }
    return grade;
}