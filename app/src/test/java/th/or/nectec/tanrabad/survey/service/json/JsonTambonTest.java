package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.LoganSquare;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Polygon;
import th.or.nectec.tanrabad.entity.Subdistrict;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class JsonTambonTest {
    @Test
    public void testParseToJsonString() throws Exception {
        JsonTambon jsonTambon = LoganSquare.parse(ResourceFile.read("tambon.json"), JsonTambon.class);

        assertEquals("840212", jsonTambon.tambonCode);
        assertEquals("ทุ่งรัง", jsonTambon.tambonName);
        assertEquals("8402", jsonTambon.amphurCode);
    }

    @Test
    public void testParseToTambonEntity() throws Exception {
        JsonTambon jsonTambon = LoganSquare.parse(ResourceFile.read("tambon.json"), JsonTambon.class);
        Subdistrict subdistrict = jsonTambon.getEntity();

        assertEquals("840212", subdistrict.getCode());
        assertEquals("ทุ่งรัง", subdistrict.getName());
        assertEquals("8402", subdistrict.getDistrictCode());
        assertEquals(stubTambonPolygon(), subdistrict.getBoundary().get(0));
    }

    public Polygon stubTambonPolygon() {
        ArrayList<Location> boundary = new ArrayList<>();
        boundary.add(new Location(9.10229074425328, 99.3916034624674));
        boundary.add(new Location(9.10146829966842, 99.3925836929561));
        boundary.add(new Location(9.10105544929067, 99.3943237020771));
        boundary.add(new Location(9.10045011749561, 99.3947399633538));
        boundary.add(new Location(9.09997408878659, 99.3952877220132));
        boundary.add(new Location(9.0997850842078, 99.3959916806683));
        boundary.add(new Location(9.09896524423992, 99.3971902911301));
        boundary.add(new Location(9.098360333479, 99.3992861213607));
        boundary.add(new Location(9.09777700044074, 99.3997382162748));
        boundary.add(new Location(9.09780731026698, 99.4004435293071));
        boundary.add(new Location(9.09826514286464, 99.40102135404));
        boundary.add(new Location(9.0980979198661, 99.4024572976596));
        boundary.add(new Location(9.09706711222566, 99.402680242997));
        boundary.add(new Location(9.09576156740199, 99.403932929405));
        boundary.add(new Location(9.09507787530777, 99.4041431752134));
        boundary.add(new Location(9.09400225831964, 99.4040151234832));
        boundary.add(new Location(9.09244533853305, 99.4030612777384));
        boundary.add(new Location(9.09211818757144, 99.4029096158608));
        boundary.add(new Location(9.09175631204151, 99.4029695209658));
        boundary.add(new Location(9.09146267656519, 99.4031989519563));
        boundary.add(new Location(9.09186025046244, 99.40459285961));
        boundary.add(new Location(9.09170964653049, 99.4052979576289));
        boundary.add(new Location(9.09084399023448, 99.4064595303805));
        boundary.add(new Location(9.08977275467264, 99.4074524973435));
        boundary.add(new Location(9.08945662131317, 99.4080863206088));
        boundary.add(new Location(9.08977206707966, 99.4106114011464));
        boundary.add(new Location(9.08993895367318, 99.4135111053549));
        boundary.add(new Location(9.09135184116082, 99.4187800213099));
        boundary.add(new Location(9.0917435396272, 99.4212966371582));
        boundary.add(new Location(9.09249284166361, 99.4244842798673));
        boundary.add(new Location(9.09284092482604, 99.4270077143914));
        boundary.add(new Location(9.09273211751749, 99.4291848156216));
        boundary.add(new Location(9.09196714190834, 99.4331151789132));
        boundary.add(new Location(9.09201110108325, 99.4346559988217));
        boundary.add(new Location(9.0912174463327, 99.4336745084727));
        boundary.add(new Location(9.08934389508022, 99.4323390781657));
        boundary.add(new Location(9.08664800224317, 99.430867338317));
        boundary.add(new Location(9.0845065182818, 99.4300281406591));
        boundary.add(new Location(9.08076975342211, 99.4291580576545));
        boundary.add(new Location(9.07561024701597, 99.4276783540425));
        boundary.add(new Location(9.06871092475506, 99.4274722724676));
        boundary.add(new Location(9.06266164904145, 99.4264505228614));
        boundary.add(new Location(9.05595679128921, 99.424802492494));
        boundary.add(new Location(9.05313505697218, 99.423604893552));
        boundary.add(new Location(9.04952312510869, 99.4223080367536));
        boundary.add(new Location(9.04882759471795, 99.4219802205149));
        boundary.add(new Location(9.0482374368504, 99.421484218378));
        boundary.add(new Location(9.04651105473423, 99.4189515307717));
        boundary.add(new Location(9.04592822709256, 99.418456103876));
        boundary.add(new Location(9.04523494449656, 99.4181357176296));
        boundary.add(new Location(9.03844140743842, 99.4168847442549));
        boundary.add(new Location(9.03412936140228, 99.4152823938087));
        boundary.add(new Location(9.0311449765481, 99.4128718359106));
        boundary.add(new Location(9.02869938315693, 99.4099120043805));
        boundary.add(new Location(9.02800469848591, 99.4098435421484));
        boundary.add(new Location(9.02660488300878, 99.4104692128916));
        boundary.add(new Location(9.02599441289456, 99.410926838636));
        boundary.add(new Location(9.02503066498966, 99.4138486310322));
        boundary.add(new Location(9.02401472753573, 99.4159236336797));
        boundary.add(new Location(9.02301845310461, 99.4170831418016));
        boundary.add(new Location(9.01898584996479, 99.4193030408577));
        boundary.add(new Location(9.01481675962166, 99.421256054969));
        boundary.add(new Location(9.01410695074949, 99.4215531760808));
        boundary.add(new Location(9.01122477102824, 99.422260646862));
        boundary.add(new Location(9.01007480997858, 99.4204089887715));
        boundary.add(new Location(9.00956157256644, 99.4198676314526));
        boundary.add(new Location(9.00750729520207, 99.418829762753));
        boundary.add(new Location(9.00522068660479, 99.4167778344437));
        boundary.add(new Location(9.00342332258672, 99.4154809982817));
        boundary.add(new Location(9.00298333336817, 99.4141385782362));
        boundary.add(new Location(9.00246447724331, 99.4135437591172));
        boundary.add(new Location(9.00211929343106, 99.4128570568373));
        boundary.add(new Location(9.0018903563094, 99.4113158506845));
        boundary.add(new Location(9.00088774322266, 99.4075322515705));
        boundary.add(new Location(9.00114012515365, 99.4067848125108));
        boundary.add(new Location(9.00063360594271, 99.403718273896));
        boundary.add(new Location(9.00027774651308, 99.3982483206523));
        boundary.add(new Location(9.00035124083724, 99.3966773106315));
        boundary.add(new Location(9.00078987769563, 99.3935799488951));
        boundary.add(new Location(9.00146130354789, 99.3905129760244));
        boundary.add(new Location(9.00191347278284, 99.3890105951148));
        boundary.add(new Location(9.0028768870704, 99.3867445193097));
        boundary.add(new Location(9.00566920999735, 99.3843336852203));
        boundary.add(new Location(9.00647296335729, 99.3844459578363));
        boundary.add(new Location(9.00761764207565, 99.3855776236983));
        boundary.add(new Location(9.00911432453854, 99.385849312818));
        boundary.add(new Location(9.01059697701265, 99.3854374924676));
        boundary.add(new Location(9.01121187596353, 99.3839738843165));
        boundary.add(new Location(9.01249714802654, 99.3831222917594));
        boundary.add(new Location(9.01549648262699, 99.3822400727274));
        boundary.add(new Location(9.01627339919427, 99.3809381220309));
        boundary.add(new Location(9.01942710386933, 99.381134209252));
        boundary.add(new Location(9.02344313597346, 99.3810480114716));
        boundary.add(new Location(9.02538552518772, 99.3805177943862));
        boundary.add(new Location(9.02656539047384, 99.3793418926121));
        boundary.add(new Location(9.02803717422603, 99.3790806918991));
        boundary.add(new Location(9.02900941999339, 99.3802713747101));
        boundary.add(new Location(9.02935028434132, 99.3818930010646));
        boundary.add(new Location(9.03047126743807, 99.383013304232));
        boundary.add(new Location(9.0320682944305, 99.3824503009614));
        boundary.add(new Location(9.03359052331438, 99.383128065962));
        boundary.add(new Location(9.03535240311396, 99.3860085528485));
        boundary.add(new Location(9.0382224931038, 99.3870517065451));
        boundary.add(new Location(9.0410329846268, 99.3851268845713));
        boundary.add(new Location(9.04251936557357, 99.3855032069697));
        boundary.add(new Location(9.04415274414595, 99.3853229953538));
        boundary.add(new Location(9.0458441316143, 99.3853276255442));
        boundary.add(new Location(9.04736535134968, 99.3858660780811));
        boundary.add(new Location(9.04837393034392, 99.3874276446078));
        boundary.add(new Location(9.04998547936137, 99.3875715534408));
        boundary.add(new Location(9.05428695714102, 99.3896530575168));
        boundary.add(new Location(9.05704631397552, 99.3916828888148));
        boundary.add(new Location(9.06054764587591, 99.392375971762));
        boundary.add(new Location(9.06233115582153, 99.3924268060915));
        boundary.add(new Location(9.06393278233885, 99.3928374504822));
        boundary.add(new Location(9.06679146432046, 99.3913300427278));
        boundary.add(new Location(9.06855374456373, 99.3916589750334));
        boundary.add(new Location(9.07012715812827, 99.3909799041565));
        boundary.add(new Location(9.07191250622527, 99.3913901919504));
        boundary.add(new Location(9.07392613452284, 99.3913934920876));
        boundary.add(new Location(9.0750725808946, 99.3903568030889));
        boundary.add(new Location(9.07636914418576, 99.3895278413498));
        boundary.add(new Location(9.07860832119517, 99.3884519111232));
        boundary.add(new Location(9.07990485578419, 99.3876462677911));
        boundary.add(new Location(9.08136440204074, 99.3871877338567));
        boundary.add(new Location(9.08320604108658, 99.3873080160046));
        boundary.add(new Location(9.08486671356168, 99.3880434568662));
        boundary.add(new Location(9.086092965201, 99.3868902199363));
        boundary.add(new Location(9.08759694259684, 99.3861069551824));
        boundary.add(new Location(9.09028681731929, 99.3880908120614));
        boundary.add(new Location(9.09192019491771, 99.387910000537));
        boundary.add(new Location(9.09350445659559, 99.3871262559785));
        boundary.add(new Location(9.09470623740688, 99.3882582751796));
        boundary.add(new Location(9.09568969021003, 99.3894605513889));
        boundary.add(new Location(9.09720005248615, 99.3901270950033));
        boundary.add(new Location(9.09843905533862, 99.3892172479362));
        boundary.add(new Location(9.10158003078504, 99.3890647939362));
        boundary.add(new Location(9.10126691382963, 99.3911308156832));
        boundary.add(new Location(9.10229074425328, 99.3916034624674));
        return new Polygon(boundary, null);
    }

}
