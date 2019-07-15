package demo.springboot.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Book 实体类
 *zyh
 */
@Data
@Entity
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@RequiredArgsConstructor
@Builder
public class Book implements Serializable {

    /**
     * 编号
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 合同类型 0：单签 1：双签
     */
    private String sType;

    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;

    /**
     * 出生年份
     */
    private String bYear;
    /**
     * 身份证
     */
    private String card;
    /**
     * 年龄
     */
    private String age;

    /**
     * 婚姻状况
     */
    private String mStatus;
    /**
     * 学历
     */
    private String education;
    /**
     * 电话
     */
    private String tel;

    /**
     * 户籍地址
     */
    private String pLAdd;

    /**
     * 户口所在地
     */
    private String pAdd;

    /**
     * 现居住地址
     */
    private String lCAdd;


    /**
     * 单位
     */
    private String company;
    /**
     * 单位地址
     */
    private String cAdd;


    /**
     * 姓名 配偶
     */
    private String nameP;
    /**
     * 性别配偶
     */
    private String sexP;
    /**
     * 身份证配偶
     */
    private String cardP;
    /**
     * 出生年份配偶
     */
    private String bYearP;
    /**
     * 年龄配偶
     */
    private String ageP;
    /**
     * 婚姻状况配偶
     */
    private String mStatusP;
    /**
     * 学历配偶
     */
    private String educationP;
    /**
     * 电话配偶
     */
    private String telP;

    /**
     * 户籍地址配偶
     */
    private String pLAddP;

    /**
     * 户口所在地配偶
     */
    private String pAddP;

    /**
     * 现居住地址配偶
     */
    private String lCAddP;


    /**
     * 单位配偶
     */
    private String companyP;
    /**
     * 单位地址配偶
     */
    private String cAddP;

    /**
     * 登记日期
     */
    private String RDate;
    /**
     * 车牌
     */
    private String lPlate;
    /**
     * 车辆类型
     */
    private String cType;
    /**
     * 车型号
     */
    private String vModel;
    /**
     * 车辆品牌
     */
    private String cBrand;
    /**
     * 车身颜色
     */
    private String cColor;
    /**
     * 车架号
     */
    private String Fnum;

    /**
     * 发动机号
     */
    private String eNum;
    /**
     * 燃料种类
     */
    private String FuType;
    /**
     * 总质量
     */
    private String totalQ;

    /**
     * 使用年限
     */
    private String sLife;
    /**
     * 成新率
     */
    private String nRate;
    /**
     * 车价大写
     */
    private String vPriceL;
    /**
     * 车价小写
     */
    private String vPriceM;
    /**
     * 车价小写带0000
     */
    private String vPriceML;
    /**
     * 首付款
     */
    private String DMoney;
    /**
     * 自筹款
     */
    private String SMoney;
    /**
     * 自筹款0000
     */
    private String SMoneyL;
    /**
     * 评估价
     */
    private String Valuation;


    /**
     * 合同额度大写
     */
    private String cSum;
    /**
     * 合同额度小写
     */
    private String cSumM;
    /**
     * 合同额度小写0000
     */
    private String cSumML;

    /**
     * 贷款卡号
     */
    private String lCNum;

    /**
     * 保单号
     */
    private String pNum;


    /**
     * 资产总额
     */
    private String totalA;

    /**
     * 负债总额
     */
    private String totalL;
    /**
     * 月收入
     */
    private String MI;
    /**
     * 月收入0000
     */
    private String MIL;
    /**
     * 年收入
     */
    private String YI;
    /**
     * 年支出
     */
    private String YO;

    /**
     * 借款用途
     */
    private String UOI;
    /**
     * 保证金
     */
    private String Bond;

    /**
     * 抵押率
     */
    private String MRate;

    /**
     * 建筑面积
     */
    private String Barea;
    /**
     * 任职
     */
    private String Serving;
    /**
     * 行业类型
     */
    private String IType;
    /**
     * 利息收入
     */
    private String IIncome;
    /**
     * 借款原因
     */
    private String BCause;
    /**
     * 征信详情
     */
    private String CDetails;
    /**
     * 主要经营
     */
    private String MBusiness;
    /**
     * 年
     */
    private String sYear;
    /**
     * 月
     */
    private String sMonth;
    /**
     * 日
     */
    private String sDay;
    /**
     * 年月日 yyyy/MM/dd
     */
    private String sYMD;
    /**
     * 期数
     */
    private String nop;

    /**
     * 服务费0000
     */
    private String SCML;
    /**
     * 渠道费0000
     */
    private String CFML;
}
