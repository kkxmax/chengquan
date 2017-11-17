//
//  HomeFamiliarDetailViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeFamiliarDetailViewController.h"
#import "EvalTopTableViewCell.h"
#import "EvaluateTableViewCell.h"
#import "HomeCommerceCollectionViewCell.h"
#import "HomeItemTableViewCell.h"
#import "HomeServiceTableViewCell.h"
#import "BlankTableViewCell.h"
#import "Global.h"
#import "UIImageView+WebCache.h"
#import "EvaluateDetailViewController.h"

@interface HomeFamiliarDetailViewController ()
{
    int height;
    NSMutableArray *evaluateArray;
    NSMutableArray *productArray;
    NSMutableArray *itemArray;
    NSMutableArray *serviceArray;
    NSMutableArray *evaluateHeightArray;
}
@end

enum {
    SELECT_EVAL = 0,
    SELECT_PROD,
    SELECT_ITEM,
    SELECT_SERV
};

@implementation HomeFamiliarDetailViewController
@synthesize selectType, friendDictionary;
@synthesize consOfficeDetail, imgShowHide, lblShowHide;
@synthesize btnEval, btnItem, btnProduct, btnService, sepEval, sepItem, sepProduct, sepService;
@synthesize btnFavourite, lblAllEval, lblBackEval, lblFrontEval, viewWriteMessage;
@synthesize tblItemView, tblServiceView, tblEvaluateView, collectProductView;
@synthesize scrollContentView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    // Initialize Array
    evaluateArray = [NSMutableArray array];
    productArray = [NSMutableArray array];
    itemArray = [NSMutableArray array];
    serviceArray = [NSMutableArray array];
    

    btnFavourite.layer.cornerRadius = 17.0f;
    lblAllEval.layer.cornerRadius = 12.0f;
    lblBackEval.layer.cornerRadius = 12.0f;
    lblFrontEval.layer.cornerRadius = 12.0f;
    viewWriteMessage.layer.cornerRadius = 17.0f;
    
    height = scrollContentView.frame.size.height;
    tblEvaluateView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, height)];
    [tblEvaluateView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    tblEvaluateView.dataSource = self;
    tblEvaluateView.delegate = self;
    [tblEvaluateView registerNib:[UINib nibWithNibName:@"EvaluateTableViewCell" bundle:nil] forCellReuseIdentifier:@"EvaluateTableViewCellIdentifier"];
    [tblEvaluateView registerNib:[UINib nibWithNibName:@"EvalTopTableViewCell" bundle:nil] forCellReuseIdentifier:@"EvalTopCellIdentifier"];
    [tblEvaluateView registerNib:[UINib nibWithNibName:@"BlankTableViewCell" bundle:nil] forCellReuseIdentifier:@"BlankCellIdentifier"];
    [scrollContentView addSubview:tblEvaluateView];
    
    [self createProductView];
    [self createItemView];
    [self createServiceView];
    [scrollContentView setContentSize:CGSizeMake(SCREEN_WIDTH * 4, height)];
    scrollContentView.delegate = self;
    
    selectType = SELECT_EVAL;
    [self onClickEvalButton:nil];
    
    [self getDataFromServer];
    
    // initialize notification
    
    // NSNotification for EvaluteDetailView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showMoreReplyView:) name:SHOW_MORE_REPLY_VIEW_NOTIFICATION object:nil];
    // NSNotification for EvaluteTopTableViewCell
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showAllFrontBackView:) name:SHOW_ALLFRONTBACK_VIEW_NOTIFICATION object:nil];

}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma -mark NSNotification
- (void)showMoreReplyView:(NSNotification *) notification {
    if(self.tblEvaluateView) {
        [self.tblEvaluateView reloadData];
    }
}

- (void)showAllFrontBackView:(NSNotification *) notification {
/*    NSString *strFrontBackIndex = (NSString *)(notification.object);
    NSInteger nFrontBackIndex = [strFrontBackIndex integerValue];
    switch (nFrontBackIndex) {
        case 0:
            evaluateArray = (NSMutableArray *)friendDictionary[@"estimates"];
            break;
        case 1:
        {
            NSMutableArray *tmpEvaluateArray = (NSMutableArray *)friendDictionary[@"estimates"];
            for(int i = 0; i < tmpEvaluateArray.count; i++) {
                NSDictionary *tmpDic = (NSDictionary *)(tmpEvaluateArray[i]);
                if([tmpDic[@"kind"] intValue] == 1) {
                    [evaluateArray addObject:tmpDic];
                }
            }
        }
            break;
        case 2:
        {
            NSMutableArray *tmpEvaluateArray = (NSMutableArray *)friendDictionary[@"estimates"];
            for(int i = 0; i < tmpEvaluateArray.count; i++) {
                NSDictionary *tmpDic = (NSDictionary *)(tmpEvaluateArray[i]);
                if([tmpDic[@"kind"] intValue] == 2) {
                    [evaluateArray addObject:tmpDic];
                }
            }
        }
            break;
        default:
            break;
    }
    [tblEvaluateView reloadData];*/
}

- (void)setData {
    NSString *logoImageName = friendDictionary[@"logo"];
    NSInteger aKind = [friendDictionary[@"akind"] integerValue];
    [self.imgAvatar sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]]];
    if (aKind == PERSONAL_KIND) {
        [self.imgOffice setImage:[UIImage imageNamed:@"personal"]];
        self.lblName.text = friendDictionary[@"realname"];
        int nReqCodeSenderAKind = [friendDictionary[@"reqCodeSenderAkind"] intValue];
        self.lblReqCodeSender.text = [NSString stringWithFormat:@"%d度好友-%@", nReqCodeSenderAKind, friendDictionary[@"reqCodeSenderRealname"]];
    }else {
        [self.imgOffice setImage:[UIImage imageNamed:@"office"]];
        self.lblName.text = friendDictionary[@"enterName"];
        self.lblRecommend.text = friendDictionary[@"recommend"];
        self.lblOfficeInfo.text = friendDictionary[@"comment"];
        self.lblMainJob.text = friendDictionary[@"mainJob"];
        self.lblWeiXin.text = friendDictionary[@"weixin"];
        self.lblBossName.text = friendDictionary[@"boss_name"];
        self.lblBossJob.text = friendDictionary[@"boss_job"];
        self.lblBossMobile.text = friendDictionary[@"boss_mobile"];
        self.lblBossWeiXin.text = friendDictionary[@"boss_weixin"];
        self.lblAddr.text = friendDictionary[@"addr"];
        int nReqCodeSenderAKind = [friendDictionary[@"reqCodeSenderAkind"] intValue];
        self.lblReqCodeSender.text = [NSString stringWithFormat:@"%d度好友-%@", nReqCodeSenderAKind, friendDictionary[@"reqCodeSenderEnterName"]];
        NSString *certImageName = friendDictionary[@"enterCertImage"];
        [self.imgBusinessCert sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, certImageName]]];
    }
    [self.lblName sizeToFit];
    [self.btnXYName setTitle:friendDictionary[@"xyName"] forState:UIControlStateNormal];
    dispatch_async(dispatch_get_main_queue(), ^{
        CGRect nameLabelFrame = self.lblName.frame;
        CGSize stringSize = [self.btnXYName.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.0]}];
        [self.btnXYName setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 3, nameLabelFrame.origin.y, stringSize.width, 16)];
    });
    self.lblCode.text = friendDictionary[@"code"];
    self.lblViewCount.text = [NSString stringWithFormat:@"%ld%%", (long)[friendDictionary[@"credit"] longValue]];
    self.lblElectCount.text = [NSString stringWithFormat:@"%ld", (long)[friendDictionary[@"electCnt"] longValue]];
    self.lblFeedbackCount.text = [NSString stringWithFormat:@"%ld", (long)[friendDictionary[@"feedbackCnt"] longValue]];
    self.lblWebUrl.text = friendDictionary[@"weburl"];
    
    evaluateArray = (NSMutableArray *)friendDictionary[@"estimates"];
    itemArray = (NSMutableArray *)friendDictionary[@"items"];
    productArray = (NSMutableArray *)friendDictionary[@"products"];
    serviceArray = (NSMutableArray *)friendDictionary[@"services"];
    evaluateHeightArray = [NSMutableArray arrayWithCapacity:evaluateArray.count];
    [tblEvaluateView reloadData];
    [collectProductView reloadData];
    [tblItemView reloadData];
    [tblServiceView reloadData];
}
- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)getDataFromServer {
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getAccountDetail" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].selectedFriendAccountID forKey:@"accountId"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETACCOUNTDETAIL Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [self.noNetworkView setHidden:YES];
                friendDictionary = (NSDictionary *)(dicRes[@"account"]);
                [self setData];
            }else{
                [self.noNetworkView setHidden:NO];
            }
        }
    }];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

#pragma mark -  UITableViewDelegate & UITableViewDataSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    if (tableView == tblEvaluateView) {
        if(evaluateArray.count > 0)
            return 2;
        else
            return 1;
    }
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (tableView == tblEvaluateView) {
        if ( section == 0 )
            return 1;
        else
            return evaluateArray.count;
    } else if (tableView == tblItemView) {
        return itemArray.count;
    } else {
        return serviceArray.count;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (tableView == tblEvaluateView)
    {
        if ( indexPath.section == 0) {
            if(evaluateArray.count > 0)
                return 45.f;
            else
                return 390.f;
        }
        else
        {
            NSDictionary *evaluateDic = (NSDictionary *)[evaluateArray objectAtIndex:indexPath.row];
            NSMutableArray *replyArray = evaluateDic[@"replys"];
            if(replyArray.count == 0) {
                return 247.f;
            }else{
                CGFloat replyHeight = 0.f;
                if(evaluateHeightArray.count > indexPath.row) {
                    NSString *strReplyHeight = (NSString *) [evaluateHeightArray objectAtIndex:indexPath.row];
                    replyHeight = [strReplyHeight floatValue];
                }
                if(replyArray.count > 1) {
                    replyHeight += 40;
                }
                return 247.f + replyHeight;
            }
        }
    }
    else if (tableView == tblItemView)
        //return 155.f;
        return 155.f;
    else
        return 155.f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [[UITableViewCell alloc] init];
    
    if ( tableView == tblEvaluateView)
    {
        if (indexPath.section == 0) {
            if(evaluateArray.count > 0) {
                static NSString *simpleTableIdentifier = @"EvalTopCellIdentifier";
                EvalTopTableViewCell *cell = (EvalTopTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
                return cell;
            }else{
                static NSString *simpleTableIdentifier = @"BlankCellIdentifier";
                BlankTableViewCell *cell = (BlankTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
                return cell;
            }
        }
        else {
            static NSString *simpleTableIdentifier = @"EvaluateTableViewCellIdentifier";
            EvaluateTableViewCell *cell = (EvaluateTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier forIndexPath:indexPath];
            cell.moreReplyView.hidden = YES;
            cell.replyContentLabel.hidden = YES;
            NSDictionary *evaluateDic = (NSDictionary *)[evaluateArray objectAtIndex:indexPath.row];
            NSInteger ownerAkind = [evaluateDic[@"ownerAkind"] integerValue];
            if(ownerAkind == PERSONAL_KIND) {
                cell.ownerNameLabel.text = evaluateDic[@"ownerRealname"];
            }else{
                cell.ownerNameLabel.text = evaluateDic[@"ownerEnterName"];
            }
            [cell.ownerNameLabel sizeToFit];
            cell.ownerContentTextView.text = evaluateDic[@"content"];
            cell.evaluateKindLabel.text = evaluateDic[@"kindName"];
            NSString *logoImageName = evaluateDic[@"ownerLogo"];
            if(logoImageName)
                [cell.avatarImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]]];
            
            if([evaluateDic[@"isFalse"] longValue] == 1) {
                cell.isFalseImageView.hidden = NO;
            }else{
                cell.isFalseImageView.hidden = YES;
            }
            NSMutableArray *evaluateImageArray = evaluateDic[@"imgPaths"];
            for (int i = 0; i < evaluateImageArray.count; i++)
            {
                UIImageView *imgView = [[UIImageView alloc] init];
                [imgView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [evaluateImageArray objectAtIndex:i]]]];
                [imgView setFrame:CGRectMake(i * 90, 0, 80, 80)];
                [cell.scrollThumb addSubview:imgView];
            }
            [cell.scrollThumb setContentSize:CGSizeMake(evaluateImageArray.count * 90 - 10, 80)];
            
            NSString *timeText = evaluateDic[@"writeTimeString"];
            cell.writeTimeLabel.text = [timeText substringWithRange:NSMakeRange(0, 10)];
            cell.electCountLabel.text = [NSString stringWithFormat:@"(%ld)", [evaluateDic[@"electCnt"] longValue]];
            NSMutableArray *replyArray = evaluateDic[@"replys"];
            if(replyArray.count > 0) {
                cell.replyContentLabel.hidden = NO;
                if(replyArray.count > 1) {
                    cell.moreReplyView.hidden = NO;
                    cell.moreReplyLabel.text = [NSString stringWithFormat:@"展开全部回复（%d条）>", (int)(replyArray.count - 1)];
                }else{
                    cell.moreReplyView.hidden = YES;
                }
                NSString *totalContent = @"";
                for(int i = 0; i < replyArray.count; i++) {
                    NSDictionary *replyDic = (NSDictionary *)[replyArray objectAtIndex:0];
                    NSInteger ownerAkind = [replyDic[@"ownerAkind"] integerValue];
                    NSString *ownerName;
                    if(ownerAkind == PERSONAL_KIND) {
                        ownerName = replyDic[@"ownerRealname"];
                    }else{
                        ownerName = replyDic[@"ownerEnterName"];
                    }
                    NSString *replyContent = replyDic[@"content"];
                    NSString *replyText = [NSString stringWithFormat:@"%@ : %@", ownerName, replyContent];
                    totalContent = [NSString stringWithFormat:@"%@%@\n\n", totalContent, replyText];
                    if(!cell.moreReplyButton.selected)
                        break;
                }
                dispatch_async(dispatch_get_main_queue(), ^{
                    [self.tblEvaluateView beginUpdates];
                    cell.replyContentLabel.text = totalContent;
                    [cell.replyContentLabel sizeToFit];
                    NSString *strReplyHeight = [NSString stringWithFormat:@"%f", cell.replyContentLabel.frame.size.height];
                    if(evaluateHeightArray.count > indexPath.row)
                        [evaluateHeightArray replaceObjectAtIndex:indexPath.row withObject:strReplyHeight];
                    else if(evaluateHeightArray.count == indexPath.row)
                        [evaluateHeightArray insertObject:strReplyHeight atIndex:indexPath.row];
                    [self.tblEvaluateView endUpdates];
                });
            }
            return cell;
        }
    }
    else if ( tableView == tblItemView )
    {
        if(itemArray.count > 0) {
            static NSString *simpleTableIdentifier = @"HomeItemCellIdentifier";
            HomeItemTableViewCell *cell = (HomeItemTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
            NSDictionary *itemDic = (NSDictionary *)[itemArray objectAtIndex:indexPath.row];
            NSInteger aKind = [itemDic[@"akind"] integerValue];
            if (aKind == PERSONAL_KIND) {
                cell.nameLabel.text = itemDic[@"realname"];
            }else {
                cell.nameLabel.text = itemDic[@"enterName"];
            }
            [cell.nameLabel sizeToFit];
            [cell.fenleiButton setTitle:itemDic[@"fenleiName"] forState:UIControlStateNormal];
            dispatch_async(dispatch_get_main_queue(), ^{
                CGRect nameLabelFrame = cell.nameLabel.frame;
                CGSize stringSize = [cell.fenleiButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.0]}];
                [cell.fenleiButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 3, nameLabelFrame.origin.y, stringSize.width, 16)];
            });
            NSString *logoImageName = itemDic[@"logo"];
            if(logoImageName) {
                [cell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]]];
                cell.noImageLabel.hidden = YES;
            }else{
                cell.noImageLabel.hidden = NO;
            }

            cell.commentTextView.text = itemDic[@"comment"];
            return cell;
        }else{
            static NSString *simpleTableIdentifier = @"BlankTableViewCell";
            BlankTableViewCell *cell = (BlankTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
            if (cell == nil) {
                NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"BlankTableViewCell" owner:self options:nil];
                cell = [nib objectAtIndex:0];
                cell.backgroundColor = [UIColor clearColor];
            }
            return cell;
        }
    }
    else if ( tableView == tblServiceView)
    {
        if(serviceArray.count > 0) {
            static NSString *simpleTableIdentifier = @"HomeServiceCellIdentifier";
            HomeServiceTableViewCell *cell = (HomeServiceTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
            NSDictionary *serviceDic = (NSDictionary *)[serviceArray objectAtIndex:indexPath.row];
            NSInteger aKind = [serviceDic[@"akind"] integerValue];
            if (aKind == PERSONAL_KIND) {
                cell.nameLabel.text = serviceDic[@"realname"];
            }else {
                cell.nameLabel.text = serviceDic[@"enterName"];
            }
            [cell.nameLabel sizeToFit];
            [cell.fenleiButton setTitle:serviceDic[@"fenleiName"] forState:UIControlStateNormal];
            dispatch_async(dispatch_get_main_queue(), ^{
                CGRect nameLabelFrame = cell.nameLabel.frame;
                CGSize stringSize = [cell.fenleiButton.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.0]}];
                [cell.fenleiButton setFrame:CGRectMake(nameLabelFrame.origin.x + nameLabelFrame.size.width + 3, nameLabelFrame.origin.y, stringSize.width, 16)];
            });
            NSString *logoImageName = serviceDic[@"logo"];
            if(logoImageName) {
                [cell.logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]]];
                cell.noImageLabel.hidden = YES;
            }else{
                cell.noImageLabel.hidden = NO;
            }
            
            cell.commentTextView.text = serviceDic[@"comment"];
            
            return cell;
        }else{
            static NSString *simpleTableIdentifier = @"BlankTableViewCell";
            BlankTableViewCell *cell = (BlankTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
            if (cell == nil) {
                NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"BlankTableViewCell" owner:self options:nil];
                cell = [nib objectAtIndex:0];
                cell.backgroundColor = [UIColor clearColor];
            }
            return cell;
        }
    }

    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (selectType == SELECT_EVAL) {
        EvaluateDetailViewController *detailViewController = [[EvaluateDetailViewController alloc] initWithNibName:@"EvaluateDetailViewController" bundle:nil];
        NSMutableDictionary *evaluateDic = (NSMutableDictionary *)[evaluateArray objectAtIndex:indexPath.row];
        detailViewController.dicEvalData = evaluateDic;
        [self.navigationController pushViewController:detailViewController animated:YES];
    }
}

#pragma mark - UIScrollViewDelegate

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    if ( scrollView == scrollContentView ) {
        CGFloat pageWidth = scrollView.frame.size.width;
        float fractionalPage = scrollView.contentOffset.x / pageWidth;
        NSInteger page = lround(fractionalPage);
        
        if (page == SELECT_EVAL) {
            selectType = SELECT_EVAL;
        }
        else if (page == SELECT_PROD)
        {
            selectType = SELECT_PROD;
        }
        else if (page == SELECT_ITEM)
        {
            selectType = SELECT_ITEM;
        }
        else if (page == SELECT_SERV)
        {
            selectType = SELECT_SERV;
        }
        
        [self selectItem:selectType];
    }
}

#pragma mark - UICollectionView Delegate, DataSource
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    HomeCommerceCollectionViewCell *homeCommerceCollectionCell = (HomeCommerceCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"HomeCommerceCellIdentifier" forIndexPath:indexPath];
    NSDictionary *productDic = (NSDictionary *)[productArray objectAtIndex:indexPath.row];
    NSString *productImageName = productDic[@"imgPath1"];
    [homeCommerceCollectionCell.produceImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, productImageName]]];
    homeCommerceCollectionCell.produceNameLabel.text = productDic[@"name"];
    homeCommerceCollectionCell.producePriceLabel.text = [NSString stringWithFormat:@"%ld", (long)[productDic[@"price"] longValue]];
    return homeCommerceCollectionCell;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return productArray.count;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    {
        if(IS_IPHONE_5)
        {
            return CGSizeMake(156.f, 200);
        }
        else if(IS_IPHONE_6)
        {
            return CGSizeMake(184.f, 244);
        }
        else if(IS_IPHONE_6P)
        {
            return CGSizeMake(202.f, 244);
        }
    }
    return CGSizeMake(184.f, 244);
}

- (IBAction)onClickShowHideButton:(id)sender {
    
    if ( consOfficeDetail.constant > 0) {
        [UIView animateWithDuration:3.0f animations:^{
            consOfficeDetail.constant = 0.f;
            [imgShowHide setImage:[UIImage imageNamed:@"arrow_down"]];
            [lblShowHide setText:@"展开"];
            self.viewOfficeDetail.hidden = YES;
            
        }];
    } else
    {
        self.viewOfficeDetail.hidden = NO;
        [UIView animateWithDuration:3.0f animations:^{
            consOfficeDetail.constant = 478.f;
            [imgShowHide setImage:[UIImage imageNamed:@"arrow_up"]];
            [lblShowHide setText:@"收起"];
            
        }];
    }
}

- (void) selectItem:(NSInteger) type {
    [btnEval.titleLabel setTextColor:BLACK_COLOR_153];
    [btnProduct.titleLabel setTextColor:BLACK_COLOR_153];
    [btnItem.titleLabel setTextColor:BLACK_COLOR_153];
    [btnService.titleLabel setTextColor:BLACK_COLOR_153];
    
    sepEval.hidden = YES;
    sepProduct.hidden = YES;
    sepItem.hidden = YES;
    sepService.hidden = YES;
    
    if (type == SELECT_EVAL) {
        [btnEval.titleLabel setTextColor:BLACK_COLOR_51];
        sepEval.hidden = NO;
    }
    else if (type == SELECT_PROD) {
        [btnProduct.titleLabel setTextColor:BLACK_COLOR_51];
        sepProduct.hidden = NO;
    }
    else if (type == SELECT_ITEM) {
        [btnItem.titleLabel setTextColor:BLACK_COLOR_51];
        sepItem.hidden = NO;
    }
    else if (type == SELECT_SERV) {
        [btnService.titleLabel setTextColor:BLACK_COLOR_51];
        sepService.hidden = NO;
    }

}

- (IBAction)onClickEvalButton:(id)sender {
    
    selectType = SELECT_EVAL;
    
    [self selectItem:selectType];
    
    [tblEvaluateView reloadData];
    [scrollContentView setContentOffset:CGPointMake(0, 0)];

}

- (void) createProductView {
    if ( collectProductView == nil ) {
        
        UICollectionViewFlowLayout* flowLayout = [[UICollectionViewFlowLayout alloc]init];
        flowLayout.itemSize = CGSizeMake(100, 100);
        flowLayout.minimumInteritemSpacing = 7.0f;
        flowLayout.minimumLineSpacing = 9.0f;
        collectProductView = [[UICollectionView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH, 0, SCREEN_WIDTH, height) collectionViewLayout:flowLayout];
        [collectProductView setBackgroundColor:[UIColor clearColor]];
        [collectProductView registerNib:[UINib nibWithNibName:@"HomeCommerceCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"HomeCommerceCellIdentifier"];
        
        //collectProductView = [[UICollectionView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH, 0, SCREEN_WIDTH, height)];
        collectProductView.dataSource = self;
        collectProductView.delegate = self;
        
        [scrollContentView addSubview:collectProductView];
    }
}

- (IBAction)onClickProductButton:(id)sender {
    
    selectType = SELECT_PROD;
    
    [self selectItem:selectType];

    [collectProductView reloadData];
    [scrollContentView setContentOffset:CGPointMake(SCREEN_WIDTH, 0)];
}

- (void) createItemView {
    
    if (tblItemView == nil) {
        tblItemView = [[UITableView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH * 2, 0, SCREEN_WIDTH, height)];
        [tblItemView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
        tblItemView.dataSource = self;
        tblItemView.delegate = self;
        [tblItemView registerNib:[UINib nibWithNibName:@"HomeItemTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeItemCellIdentifier"];
        [scrollContentView addSubview:tblItemView];
    }
}

- (IBAction)onClickItemButton:(id)sender {
    
    selectType = SELECT_ITEM;
    
    [self selectItem:selectType];

    [tblItemView reloadData];
    
    [scrollContentView setContentOffset:CGPointMake(SCREEN_WIDTH * 2, 0)];
    
}

- (void) createServiceView {
    
    if (tblServiceView == nil) {
        
        tblServiceView = [[UITableView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH * 3, 0, SCREEN_WIDTH, height)];
        [tblServiceView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
        tblServiceView.dataSource = self;
        tblServiceView.delegate = self;
        [tblServiceView registerNib:[UINib nibWithNibName:@"HomeServiceTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeServiceCellIdentifier"];
        [scrollContentView addSubview:tblServiceView];
    }
}

- (IBAction)onClickServiceButton:(id)sender {
    
    selectType = SELECT_SERV;
    
    [self selectItem:selectType];

    [tblServiceView reloadData];
    [scrollContentView setContentOffset:CGPointMake(SCREEN_WIDTH * 3, 0)];
    
}

- (IBAction)onClickFavouriteButton:(id)sender {
}


@end
