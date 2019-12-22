package egitim.uniyaz.ui.views;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import egitim.uniyaz.MyUI;
import egitim.uniyaz.dao.UyeKitapDao;
import egitim.uniyaz.domain.Kitap;
import egitim.uniyaz.domain.KitapOkumaState;
import egitim.uniyaz.domain.Kullanici;
import egitim.uniyaz.domain.UyeKitap;

import java.util.*;

public class UyeKitapGuncelle extends VerticalLayout {

    private Table table;
    private IndexedContainer indexedContainer;

    FormLayout formLayout;


    private ComboBox okumaStateCombo;

private  UyeKitap uyeKitap;
    Kullanici kullanici = KullaniciGirisView.kullanici;

    public UyeKitapGuncelle() {

        createTable();
        insertTable();
        createCombos();
        createButton();
        addComponent(formLayout);
    }

    private  void createTable() {
        formLayout=new FormLayout();
        formLayout.setMargin(true);
        formLayout.addStyleName("outlined");
        formLayout.setSizeFull();

        table = new Table();
        table.setSelectable(true);

        indexedContainer = new IndexedContainer();
        indexedContainer.addContainerProperty("Kitap",String.class,null);
        indexedContainer.addContainerProperty("OkumaState",Enum.class,null);

        table.setContainerDataSource(indexedContainer);
        table.setColumnHeaders("Kitap","Okuma Durumu");

        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {

                uyeKitap = (UyeKitap) itemClickEvent.getItemId();
                okumaStateCombo.setValue(uyeKitap.getKitapOkumaState());

            }
        });
        table.setPageLength(table.size());
        formLayout.addComponent(table);
    }


    private  void insertTable(){
        UyeKitapDao uyeKitapDao = new UyeKitapDao();

        List<UyeKitap> uyeKitapList = uyeKitapDao.findAllKitapByKullanici(kullanici);

        for (UyeKitap uyeKitap : uyeKitapList) {

          if(KitapOkumaState.Okudum != uyeKitap.getKitapOkumaState()) {

              Item item = indexedContainer.addItem(uyeKitap);

              item.getItemProperty("Kitap").setValue(uyeKitap.getKitap().getName());
              item.getItemProperty("OkumaState").setValue(uyeKitap.getKitapOkumaState());

          }

        }
    }

    private  void createButton(){
        Button button1=new Button();
        button1.setCaption("Güncelle");
        button1.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                updateButton();
                UyeKitapDao uyeKitapDao=new UyeKitapDao();
                uyeKitap=uyeKitapDao.updateUyeKitap(uyeKitap);
            }
        });
        formLayout.addComponent(button1);
    }

    private void updateButton() {

           KitapOkumaState sonuc= (KitapOkumaState) okumaStateCombo.getValue();
            if (sonuc==KitapOkumaState.OkumayaBasladim){
                uyeKitap.setKitapOkumaState(sonuc);
                Date now = new Date();
                uyeKitap.setBaslangicTarihi(now);
            }
            else if (sonuc==KitapOkumaState.Okudum){
                uyeKitap.setKitapOkumaState(sonuc);
                Date now = new Date();
                if(uyeKitap.getBaslangicTarihi()!=null){
                    long gun=(now.getDay())-(uyeKitap.getBaslangicTarihi().getDay());
                    uyeKitap.setGun(gun);
                }
                else{
                    uyeKitap.setGun(0);
                }

            }
      }

    private  void createCombos(){
        List<KitapOkumaState> durumList = new ArrayList<KitapOkumaState>();
        durumList.addAll(Arrays.asList(KitapOkumaState.values()));

        okumaStateCombo = new ComboBox("kategori sec",durumList);
        okumaStateCombo.setWidth(30, Unit.PERCENTAGE);
        formLayout.addComponent(okumaStateCombo);
    }



}