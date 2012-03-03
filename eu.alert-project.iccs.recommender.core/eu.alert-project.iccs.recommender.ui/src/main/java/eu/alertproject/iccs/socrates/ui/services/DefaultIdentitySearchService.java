package eu.alertproject.iccs.socrates.ui.services;

import eu.alertproject.iccs.socrates.ui.bean.IdentityBean;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 08/02/12
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("identitySearchService")
public class DefaultIdentitySearchService implements IdentitySearchService{

    final Map<String,List<IdentityBean>> map;

    public DefaultIdentitySearchService() {


        map = new HashMap<String,List<IdentityBean>>();

        List<IdentityBean> one = new ArrayList<IdentityBean>();

        one.add(new IdentityBean(
                DigestUtils.md5Hex(new Date().toString()),
                "Dario","Freddi"));

        one.add(new IdentityBean(
                DigestUtils.md5Hex(new Date().toString()),
                "Fotis","Paraskevopoulos"));

        one.add(new IdentityBean(
                DigestUtils.md5Hex(new Date().toString()),
                "Dimitris","Panagiotou"));

        map.put("Top-20",one);

        
        List<IdentityBean> two = new ArrayList<IdentityBean>();

        two.add(new IdentityBean(
                DigestUtils.md5Hex(new Date().toString()),
                "David","Faure"));

        two.add(new IdentityBean(
                DigestUtils.md5Hex(new Date().toString()),
                "Kostas","Christidis"));


        map.put("Hackers",two);


        List<IdentityBean> three = new ArrayList<IdentityBean>();

        three.add(new IdentityBean(
                DigestUtils.md5Hex(new Date().toString()),
                "Dario","Andes"));

        three.add(new IdentityBean(
                DigestUtils.md5Hex(new Date().toString()),
                "Will","Stepherson"));

        three.add(new IdentityBean(
                DigestUtils.md5Hex(new Date().toString()),
                "Alex","Fiestas"));


        map.put("Bug-Triagers",three);


    }

    @Override
    public List<IdentityBean> findByForClass(String classification){

        if(!map.containsKey(classification)){
            return new ArrayList<IdentityBean>();
        }

        return map.get(classification);

    }
}
