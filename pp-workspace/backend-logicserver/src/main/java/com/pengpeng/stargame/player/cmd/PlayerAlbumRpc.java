package com.pengpeng.stargame.player.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.role.album.AlbumItemVO;
import com.pengpeng.stargame.vo.role.album.AlbumPageVO;
import com.pengpeng.stargame.vo.role.album.AlbumReq;
import com.pengpeng.stargame.vo.role.album.AlbumVO;
import com.pengpeng.user.AlbumProfile;
import com.pengpeng.user.service.AlbumItemProfile;
import com.pengpeng.user.service.IUserDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-12-3
 * Time: 下午3:09
 */
@Component()
public class PlayerAlbumRpc extends RpcHandler{
    @Autowired
    @Qualifier("remotingUserDataService")
    private IUserDataService dataService;
    @Autowired
    private IPlayerDao playerDao;

    @RpcAnnotation(cmd="p.getAlbumPage",lock = false,req=AlbumReq.class,name="取得相册列表")
    public AlbumVO[] getAlbumPage(Session session,AlbumReq albumReq) throws AlertException {
        Player player=playerDao.getBean(albumReq.getPid());
        int maxSize=dataService.countAlbumByUserId(player.getUserId());
        List<AlbumProfile> albumProfileList=dataService.findAlbumByUserId(player.getUserId(),0,maxSize);
        List<AlbumVO> albumVOList=new ArrayList<AlbumVO>();
        for(AlbumProfile albumProfile:albumProfileList){
            AlbumVO albumVO=new AlbumVO();
            BeanUtils.copyProperties(albumProfile,albumVO);
            albumVOList.add(albumVO);
        }
        return albumVOList.toArray(new AlbumVO[0]);
    }

    @RpcAnnotation(cmd="p.getAlbumItemPage",lock = false,req=AlbumReq.class,name="取得相册相片列表")
    public AlbumItemVO[] getAlbumItemPage(Session session,AlbumReq albumReq) throws AlertException {
        Player player=playerDao.getBean(albumReq.getPid());
        int maxSize=dataService.countAlbumItem(albumReq.getAlbumId());
        List<AlbumItemProfile> albumItemProfileList=dataService.findAlbumItem(albumReq.getAlbumId(),0,maxSize);
        List<AlbumItemVO> albumVOList=new ArrayList<AlbumItemVO>();
        for(AlbumItemProfile albumProfile:albumItemProfileList){
            AlbumItemVO albumVO=new AlbumItemVO();
            BeanUtils.copyProperties(albumProfile,albumVO);
            albumVOList.add(albumVO);
        }
        return albumVOList.toArray(new AlbumItemVO[0]);
    }

}
