//
//  HomeFamiliarDetailViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeFamiliarDetailViewController : UIViewController<UITableViewDelegate, UITableViewDataSource, UIScrollViewDelegate, UICollectionViewDelegate, UICollectionViewDataSource>

@property (nonatomic, strong) IBOutlet UILabel *navTitleLabel;
@property (nonatomic, strong) IBOutlet UIScrollView *bgScrollView;
@property (nonatomic, strong) IBOutlet NSDictionary *friendDictionary;
@property (weak, nonatomic) IBOutlet UIImageView *imgAvatar;
@property (weak, nonatomic) IBOutlet UIImageView *imgOffice;
@property (weak, nonatomic) IBOutlet UILabel *lblName;
@property (weak, nonatomic) IBOutlet UIButton *btnXYName;
@property (weak, nonatomic) IBOutlet UILabel *lblCode;
@property (nonatomic, weak) IBOutlet UILabel *lblElectCount;
@property (nonatomic, weak) IBOutlet UILabel *lblFeedbackCount;
@property (nonatomic, weak) IBOutlet UILabel *lblViewCount;
@property (nonatomic, strong) IBOutlet UIView *personalView;
@property (nonatomic, weak) IBOutlet UILabel *personalCompanyLabel;
@property (nonatomic, weak) IBOutlet UILabel *personalNetworkLabel;
@property (nonatomic, weak) IBOutlet UILabel *personalAddressLabel;
@property (nonatomic, weak) IBOutlet UILabel *personalWeixinLabel;
@property (nonatomic, weak) IBOutlet UILabel *personalReqsenderLabel;
@property (nonatomic, weak) IBOutlet UILabel *personalJobLabel;
@property (nonatomic, weak) IBOutlet UILabel *officeMarkLabel;

@property (nonatomic, strong) IBOutlet UIView *selectView;
/* Enterprise Info */
@property (nonatomic, weak) IBOutlet UILabel *lblWebUrl;
@property (nonatomic, weak) IBOutlet UILabel *lblRecommend;
@property (weak, nonatomic) IBOutlet UILabel *lblOfficeInfo;
@property (weak, nonatomic) IBOutlet UILabel *lblMainJob;
@property (weak, nonatomic) IBOutlet UILabel *lblWeiXin;
@property (weak, nonatomic) IBOutlet UILabel *lblBossName;
@property (weak, nonatomic) IBOutlet UILabel *lblBossJob;
@property (weak, nonatomic) IBOutlet UILabel *lblBossMobile;
@property (weak, nonatomic) IBOutlet UILabel *lblBossWeiXin;
@property (weak, nonatomic) IBOutlet UILabel *lblAddr;

/* Personal Info */
@property (weak, nonatomic) IBOutlet UILabel *lblReqCodeSender;
@property (weak, nonatomic) IBOutlet UIView *viewOfficeDetail;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *consOfficeDetail;
@property (weak, nonatomic) IBOutlet UIImageView *imgShowHide;
@property (weak, nonatomic) IBOutlet UILabel *lblShowHide;
@property (weak, nonatomic) IBOutlet UIButton *btnEval;
@property (weak, nonatomic) IBOutlet UIButton *btnProduct;
@property (weak, nonatomic) IBOutlet UIButton *btnItem;
@property (weak, nonatomic) IBOutlet UIButton *btnService;
@property (weak, nonatomic) IBOutlet UIButton *btnFavourite;
@property (weak, nonatomic) IBOutlet UILabel *sepEval;
@property (weak, nonatomic) IBOutlet UILabel *sepProduct;
@property (weak, nonatomic) IBOutlet UILabel *sepItem;
@property (weak, nonatomic) IBOutlet UILabel *sepService;
@property (weak, nonatomic) IBOutlet UILabel *lblAllEval;
@property (weak, nonatomic) IBOutlet UILabel *lblFrontEval;
@property (weak, nonatomic) IBOutlet UILabel *lblBackEval;
@property (weak, nonatomic) IBOutlet UIView *viewWriteMessage;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollContentView;
@property (weak, nonatomic) IBOutlet UIImageView *imgBusinessCert;

@property (retain, nonatomic) IBOutlet UIView *noNetworkView;
@property (retain, nonatomic) IBOutlet UIView *favoriteEvaluateView;

@property (retain, nonatomic) UITableView *tblEvaluateView;
@property (retain, nonatomic) UICollectionView *collectProductView;
@property (retain, nonatomic) UITableView *tblItemView;
@property (retain, nonatomic) UITableView *tblServiceView;

@property (nonatomic, weak) IBOutlet UIButton *btnEvaluate;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *scrollViewContentHeight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lblOfficeContentBottom;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *scrollContentViewHeight;

@property (weak, nonatomic) IBOutlet UIView *minimizeBarView;
@property (weak, nonatomic) IBOutlet UIView *blankView;
@property (weak, nonatomic) IBOutlet UILabel *blankTextLabel;

- (IBAction)onClickShowHideButton:(id)sender;
- (IBAction)onClickEvalButton:(id)sender;
- (IBAction)onClickProductButton:(id)sender;
- (IBAction)onClickItemButton:(id)sender;
- (IBAction)onClickServiceButton:(id)sender;
- (IBAction)onClickFavouriteButton:(id)sender;
- (IBAction)onBackAction:(id)sender;

@property (nonatomic, assign) int selectType;
@end
