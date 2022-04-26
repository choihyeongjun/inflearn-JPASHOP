package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;
    //나중에 여기 부분 널뜨면 autowired로 바꾸기 전에 했을때 required했을때 널뜸
    public void save(Item item){
        if(item.getId()==null){
            em.persist(item);
        }else{
            em.merge(item);//업데이트 같은 느낌
        }
    }
    public Item findOne(Long id){
        return em.find(Item.class,id);
    }
    public List<Item> findAll(){
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }
}
