//
//  HomeViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/23/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeViewController.h"
#import "HomeFamiliarViewController.h"
#import "HomeEnterpriseViewController.h"
#import "HomeCommerceViewController.h"
#import "HomeItemViewController.h"
#import "HomeServiceViewController.h"
#import "HomeSortViewController.h"
#import "Global.h"
#import "WebAPI.h"
#import "UIImageView+WebCache.h"
#import "AAPullToRefresh.h"

@interface HomeViewController ()
{
    HomeSortViewController *homeSortVC;
    NSMutableArray *carouselObjectArray;
    int nImageNum;
    CGRect ViewSize;
    HomeFamiliarViewController *homeFamiliarVC;
    HomeEnterpriseViewController *homeEnterpriseVC;
    HomeCommerceViewController *homeCommerceVC;
    HomeItemViewController *homeItemVC;
    HomeServiceViewController *homeServiceVC;
    BOOL isFontAdjusted;
    
    CGFloat familiarTableViewHeight;
    CGFloat enterpriseTableViewHeight;
    CGFloat commerceTableViewHeight;
    CGFloat itemTableViewHeight;
    CGFloat serviceTableViewHeight;
    
    AAPullToRefresh *topRefreshView;
    AAPullToRefresh *bottomRefreshView;
}
@end

@implementation HomeViewController


@synthesize searchBar, messageNumberLabel, homeScrollView, familiarButton, commerceButton, itemButton, serviceButton, enterpriseButton, overScrollView, slideHomeScrollView, slideHomePageCtrl;

- (void)viewDidLoad {
    [super viewDidLoad];
    isFontAdjusted = false;
    [CommonData sharedInstance].subHomeIndex = 0;
    // Do any additional setup after loading the view from its nib.
    homeFamiliarVC = [[HomeFamiliarViewController alloc] initWithNibName:@"HomeFamiliarViewController" bundle:nil];
    homeEnterpriseVC = [[HomeEnterpriseViewController alloc] initWithNibName:@"HomeEnterpriseViewController" bundle:nil];
    homeCommerceVC = [[HomeCommerceViewController alloc] initWithNibName:@"HomeCommerceViewController" bundle:nil];
    homeItemVC = [[HomeItemViewController alloc] initWithNibName:@"HomeItemViewController" bundle:nil];
    homeServiceVC = [[HomeServiceViewController alloc] initWithNibName:@"HomeServiceViewController" bundle:nil];
    homeSortVC = [[HomeSortViewController alloc] initWithNibName:@"HomeSortViewController" bundle:nil];

    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reloadHomeData) name:SET_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(hideSortView:) name:HIDE_SORT_VIEW_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showResultView:) name:SHOW_RESULT_SEARCH_VIEW_NOTIFICATION object:nil];
    
    [self setNoSelectedAllButtons];
    familiarButton.selected = YES;
    // Get Web Data
    [self getCarouselList];
    homeFamiliarVC.currentSortOrderIndex = 1;
    [self updateNotification];
    appDelegate.notificationDelegate = self;
    
    __weak typeof(self) weakSelf = self;
    topRefreshView = [self.overScrollView addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopItems];
    }];
    bottomRefreshView = [self.overScrollView addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomItems];
    }];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
    // Customize Search Bar
    //[searchBar setImage:[UIImage imageNamed:@"nav_search"] forSearchBarIcon:UISearchBarIconSearch state:UIControlStateNormal];
//    [searchBar setBackgroundImage:[UIImage imageNamed:@"nav_bg"]];
    [[UISearchBar appearance] setBackgroundImage:[UIImage imageNamed:@"transparent.png"]];

    for(UIView* i in [searchBar subviews])
    {
        if([i isKindOfClass:[UITextField class]])
        {
            [((UITextField*)i) setBorderStyle:UITextBorderStyleNone];
        }
        for(UIView* j in [i subviews])
        {
            if([j isKindOfClass:[UITextField class]])
            {
                [((UITextField*)j) setBorderStyle:UITextBorderStyleNone];
                [((UITextField*)j) setAdjustsFontSizeToFitWidth:YES];
                if(!isFontAdjusted)
                {
                    [((UITextField*)j) setFont:[UIFont systemFontOfSize:[[((UITextField*)j) font] pointSize] - 1] ];
                    isFontAdjusted = true;
                }
            }
        }
    }
    messageNumberLabel.layer.cornerRadius = messageNumberLabel.frame.size.width / 2;
    messageNumberLabel.layer.masksToBounds = YES;

    topRefreshView.showPullToRefresh = YES;
    bottomRefreshView.showPullToRefresh = YES;
    
    
    NSInteger nCurrentPage = [CommonData sharedInstance].subHomeIndex;
    switch(nCurrentPage) {
        case 0:
            [homeFamiliarVC viewWillAppear:animated];
            [self familiarButtonAction: nil];
            break;
        case 1:
            [homeEnterpriseVC viewWillAppear:animated];
            [self enterpriseButtonAction:nil];
            break;
        case 2:
            [homeCommerceVC viewWillAppear:animated];
            [self commerceButtonAction:nil];
            break;
        case 3:
            [homeItemVC viewWillAppear:animated];
            [self itemButtonAction: nil];
            break;
        case 4:
            [homeServiceVC viewWillAppear:animated];
            [self serviceButtonAction: nil];
            break;
        default:
            break;
    }

   
}
- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    topRefreshView.showPullToRefresh = NO;
    bottomRefreshView.showPullToRefresh = NO;
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // InitializePageView
    [self initPageView];
}

- (void)refreshTopItems {
    NSInteger index = [CommonData sharedInstance].subHomeIndex;
    if (index == SUB_HOME_PERSONAL) {
        [homeFamiliarVC refreshTopItems];
    }
    else if (index == SUB_HOME_ENTERPRISE) {
        [homeEnterpriseVC refreshTopItems];
    }
    else if (index == SUB_HOME_COMMERCE) {
        [homeCommerceVC refreshTopItems];
        
    }
    else if (index == SUB_HOME_ITEM) {
        [homeItemVC refreshTopItems];
    }
    else if (index == SUB_HOME_SERVICE) {
        [homeServiceVC refreshTopItems];
    }
}

- (void)refreshBottomItems {
    NSInteger index = [CommonData sharedInstance].subHomeIndex;
    if (index == SUB_HOME_PERSONAL) {
        [homeFamiliarVC refreshBottomItems];
    }
    else if (index == SUB_HOME_ENTERPRISE) {
        [homeEnterpriseVC refreshBottomItems];
    }
    else if (index == SUB_HOME_COMMERCE) {
        [homeCommerceVC refreshBottomItems];
        
    }
    else if (index == SUB_HOME_ITEM) {
        [homeItemVC refreshBottomItems];
    }
    else if (index == SUB_HOME_SERVICE) {
        [homeServiceVC refreshBottomItems];
    }
}

- (void)reloadHomeData {
    NSInteger index = [CommonData sharedInstance].subHomeIndex;
    if (index == SUB_HOME_PERSONAL) {
        [homeFamiliarVC refreshTopItems];
    }
    else if (index == SUB_HOME_ENTERPRISE) {
        [homeEnterpriseVC refreshTopItems];
    }
    else if (index == SUB_HOME_COMMERCE) {
        [homeCommerceVC refreshTopItems];

    }
    else if (index == SUB_HOME_ITEM) {
        [homeItemVC refreshTopItems];
    }
    else if (index == SUB_HOME_SERVICE) {
        [homeServiceVC refreshTopItems];
    }
}

#pragma mark - Notification Delegate
- (void)updateNotification {
    if([CommonData sharedInstance].notificationCount > 0) {
        NSString *strNotificationCount = [NSString stringWithFormat:@"%ld", [CommonData sharedInstance].notificationCount];
        messageNumberLabel.text = strNotificationCount;
        messageNumberLabel.hidden = NO;
    }else{
        messageNumberLabel.hidden = YES;
    }
}

#pragma mark - Web Function
- (void)getCarouselList {
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getCarouselList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];

    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETCAROUSELLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                carouselObjectArray = [[NSMutableArray alloc] initWithArray:(NSMutableArray *)(dicRes[@"data"])];
                // Set slide page time
                [self showSlideShow];
            }else{
                [appDelegate.window makeToast:dicRes[@"msg"]
                                                 duration:3.0
                                                 position:CSToastPositionCenter
                                                    style:nil];
            }
        }else{
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                                             duration:3.0
                                             position:CSToastPositionCenter
                                                style:nil];
        }
        [GeneralUtil hideProgress];
    }];
}
#pragma mark - Page Slide
- (void)showSlideShow {
    slideHomeScrollView.contentSize = CGSizeMake(slideHomeScrollView.frame.size.width * carouselObjectArray.count, slideHomeScrollView.frame.size.height);
    ViewSize = slideHomeScrollView.bounds;
    
    // set page control count, size and position
    [slideHomePageCtrl setNumberOfPages:carouselObjectArray.count];
    CGRect pcFrame = slideHomePageCtrl.frame;
    CGFloat width = carouselObjectArray.count * 16;
    [slideHomePageCtrl setFrame:CGRectMake(pcFrame.origin.x + pcFrame.size.width - width, pcFrame.origin.y, width, pcFrame.size.height)];
    
    nImageNum = 0;
    while (nImageNum < carouselObjectArray.count) {
        NSDictionary *carouselDic = (NSDictionary *)[carouselObjectArray objectAtIndex:nImageNum];
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:ViewSize];
        imageView.contentMode = UIViewContentModeScaleToFill;
        if(![carouselDic[@"imgUrl"] isEqualToString:@""]) {
            [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, carouselDic[@"imgUrl"]]] placeholderImage:[UIImage imageNamed:@"bg_pic.png"]];
        }
        if(![carouselDic[@"videoUrl"] isEqualToString:@""]) {
            UIImageView *videoIcon = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"play.png"]];
            [videoIcon setFrame:CGRectMake((ViewSize.size.width - videoIcon.frame.size.width) / 2, (ViewSize.size.height - videoIcon.frame.size.height) / 2, videoIcon.frame.size.width, videoIcon.frame.size.height)];
            [imageView addSubview:videoIcon];
            
        }
        [slideHomeScrollView addSubview:imageView];
        ViewSize = CGRectOffset(ViewSize, slideHomeScrollView.bounds.size.width, 0);
        nImageNum ++;
    }
    slideHomePageTimer = [NSTimer scheduledTimerWithTimeInterval:SLIDE_SECOND target:self selector:@selector(PageMove) userInfo:nil repeats:YES];
//    [GeneralUtil hideProgress];
}
- (void)PageMove {
    CGFloat pageSize = slideHomeScrollView.frame.size.width;
    NSInteger nCurrentPage = 0;
    // if this is the last page return
    if(slideHomeScrollView.contentOffset.x >= slideHomeScrollView.frame.size.width * (carouselObjectArray.count - 1)) {
        [slideHomeScrollView setContentOffset:CGPointMake(0.0, slideHomeScrollView.contentOffset.y) animated:YES];
    } else {
        nCurrentPage = (NSInteger)(slideHomeScrollView.contentOffset.x / slideHomeScrollView.frame.size.width) + 1;
        [slideHomeScrollView setContentOffset:CGPointMake(nCurrentPage * pageSize, slideHomeScrollView.contentOffset.y) animated:YES];
    }
    [slideHomePageCtrl setCurrentPage:nCurrentPage];
}

#pragma mark - Initialize Page
- (void)initPageView {
   
//    [slideHomeScrollView setFrame:CGRectMake(slideHomeScrollView.frame.origin.x, slideHomeScrollView.frame.origin.y, overScrollView.frame.size.width, slideHomeScrollView.frame.size.height)];
//    [overScrollView setContentSize:CGSizeMake(overScrollView.frame.size.width, overScrollView.frame.size.height + slideHomeScrollView.frame.size.height)];
//    [homeScrollView setFrame:CGRectMake(homeScrollView.frame.origin.x, homeScrollView.frame.origin.y, SCREEN_WIDTH, overScrollView.contentSize.height)];
    
    [homeFamiliarVC.view setFrame:CGRectMake(0, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeFamiliarVC.view];
    homeFamiliarVC.baseDelegate = self;
    
    [homeEnterpriseVC.view setFrame:CGRectMake(homeScrollView.frame.size.width, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeEnterpriseVC.view];
    homeEnterpriseVC.baseDelegate = self;
    
    [homeCommerceVC.view setFrame:CGRectMake(homeScrollView.frame.size.width * 2, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeCommerceVC.view];
    homeCommerceVC.baseDelegate = self;
    
    [homeItemVC.view setFrame:CGRectMake(homeScrollView.frame.size.width * 3, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeItemVC.view];
    homeItemVC.baseDelegate = self;
    
    [homeServiceVC.view setFrame:CGRectMake(homeScrollView.frame.size.width * 4, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeServiceVC.view];
    homeServiceVC.baseDelegate = self;
    
    [homeSortVC.view setFrame:CGRectMake(self.sortView.frame.size.width * 1, 0, self.sortView.frame.size.width, self.sortView.frame.size.height)];
    [homeScrollView setContentSize:CGSizeMake(homeScrollView.frame.size.width * 5, homeScrollView.frame.size.height)];
}

- (IBAction)swipeLeftHomeScrollView:(UISwipeGestureRecognizer *)gestureRecognizer {
    [slideHomePageTimer invalidate];
    slideHomePageTimer = nil;
    CGFloat pageWidth = slideHomeScrollView.frame.size.width;
    float fractionalPage = slideHomeScrollView.contentOffset.x / pageWidth;
    NSInteger nCurrentPage = lround(fractionalPage);
    if(nCurrentPage < carouselObjectArray.count - 1) {
        [slideHomeScrollView setContentOffset:CGPointMake(pageWidth * (nCurrentPage + 1), slideHomeScrollView.contentOffset.y) animated:YES];
        [slideHomePageCtrl setCurrentPage:nCurrentPage +1];

    }
    slideHomePageTimer = [NSTimer scheduledTimerWithTimeInterval:SLIDE_SECOND target:self selector:@selector(PageMove) userInfo:nil repeats:YES];
}
- (IBAction)swipeRightHomeScrollView:(UISwipeGestureRecognizer *)gestureRecognizer {
    [slideHomePageTimer invalidate];
    slideHomePageTimer = nil;
    CGFloat pageWidth = slideHomeScrollView.frame.size.width;
    float fractionalPage = slideHomeScrollView.contentOffset.x / pageWidth;
    NSInteger nCurrentPage = lround(fractionalPage);
    if(nCurrentPage > 0) {
        [slideHomeScrollView setContentOffset:CGPointMake(pageWidth * (nCurrentPage - 1), slideHomeScrollView.contentOffset.y) animated:YES];
        [slideHomePageCtrl setCurrentPage:nCurrentPage - 1];

    }
    slideHomePageTimer = [NSTimer scheduledTimerWithTimeInterval:SLIDE_SECOND target:self selector:@selector(PageMove) userInfo:nil repeats:YES];
}

static NSInteger previousPage = 0;
static NSInteger currentPage = 0;
#pragma mark - UIScrollViewDelegate
// called on finger up if the user dragged. velocity is in points/millisecond. targetContentOffset may be changed to adjust where the scroll view comes to rest
- (void)scrollViewWillEndDragging:(UIScrollView *)scrollView withVelocity:(CGPoint)velocity targetContentOffset:(inout CGPoint *)targetContentOffset {
    CGFloat pageWidth = scrollView.frame.size.width;
    float fractionalPage = targetContentOffset->x / pageWidth;
    currentPage = lround(fractionalPage);
    if (previousPage != currentPage) {
        [self setNoSelectedAllButtons];
        [CommonData sharedInstance].subHomeIndex = currentPage;
        previousPage = currentPage;
        [self setOverScrollViewContentSize];
        switch (currentPage) {
            case 0:
            {
                familiarButton.selected = YES;
//                [homeFamiliarVC getFriendFromServer:[CommonData sharedInstance].choiceFamiliarCity
//                                              aKind:[CommonData sharedInstance].choiceFamiliarKind
//                                       XyleixingIds:[CommonData sharedInstance].choiceFamiliarIds
//                                              Start:@""
//                                             Length:@""
//                                            Keyword:[CommonData sharedInstance].searchFamiliarText];
            }
                break;
            case 1:
            {
                enterpriseButton.selected = YES;
//                [homeEnterpriseVC getEnterFromServer:[CommonData sharedInstance].choiceEnterpriseCity
//                                           EnterKind:[CommonData sharedInstance].choiceEnterpriseKind
//                                        XyleixingIds:[CommonData sharedInstance].choiceEnterpriseIds
//                                               Start:@""
//                                              Length:@""
//                                             Keyword:[CommonData sharedInstance].searchEnterpriseText];
            }
                break;
            case 2:
            {
                commerceButton.selected = YES;
                
//                [homeCommerceVC getProductFromServer:[CommonData sharedInstance].choiceCommerceCity
//                                         PleixingIds:[CommonData sharedInstance].choiceCommerceIds
//                                               Start:@""
//                                              Length:@""
//                                             Keyword:[CommonData sharedInstance].searchProductText];
            }
                break;
            case 3:
            {
                itemButton.selected = YES;
//                [homeItemVC getItemFromServer:[CommonData sharedInstance].choiceItemCity
//                                        AKind:[CommonData sharedInstance].choiceItemKind
//                                    FenleiIds:[CommonData sharedInstance].choiceItemIds
//                                        Start:@""
//                                       Length:@""
//                                      Keyword:[CommonData sharedInstance].searchItemText];
            }
                break;
            case 4:
            {
                serviceButton.selected = YES;
//                [homeServiceVC getServiceFromServer:[CommonData sharedInstance].choiceServiceCity
//                                              AKind:[CommonData sharedInstance].choiceServiceKind
//                                          FenleiIds:[CommonData sharedInstance].choiceServiceIds
//                                              Start:@""
//                                             Length:@""
//                                            Keyword:[CommonData sharedInstance].searchServiceText];
            }
                break;
            default:
                break;
        }
    }
}
- (void)setNoSelectedAllButtons {
    familiarButton.selected = NO;
    enterpriseButton.selected = NO;
    commerceButton.selected = NO;
    itemButton.selected = NO;
    serviceButton.selected = NO;
}

#pragma mark - Button Action
- (IBAction)familiarButtonAction:(id)sender {
    if(!familiarButton.selected)
    {
        [CommonData sharedInstance].subHomeIndex = 0;
        [self setOverScrollViewContentSize];
        [self moveToPage:0];
        familiarButton.selected = YES;
        self.addButton.hidden = YES;
        self.overScrollView.contentOffset = CGPointMake(0, 0);
//        [homeFamiliarVC getFriendFromServer:[CommonData sharedInstance].choiceFamiliarCity
//                                      aKind:[CommonData sharedInstance].choiceFamiliarKind
//                               XyleixingIds:[CommonData sharedInstance].choiceFamiliarIds
//                                      Start:@""
//                                     Length:@""
//                                    Keyword:[CommonData sharedInstance].searchFamiliarText];
    }
}

- (IBAction)enterpriseButtonAction:(id)sender {
    if(!enterpriseButton.selected)
    {
        [CommonData sharedInstance].subHomeIndex = 1;
        [self setOverScrollViewContentSize];
        [self moveToPage:1];
        enterpriseButton.selected = YES;
        self.addButton.hidden = YES;
        self.overScrollView.contentOffset = CGPointMake(0, 0);
//        [homeEnterpriseVC getEnterFromServer:[CommonData sharedInstance].choiceEnterpriseCity
//                                   EnterKind:[CommonData sharedInstance].choiceEnterpriseKind
//                                XyleixingIds:[CommonData sharedInstance].choiceEnterpriseIds
//                                       Start:@""
//                                      Length:@""
//                                     Keyword:[CommonData sharedInstance].searchEnterpriseText];
    }
}

- (IBAction)commerceButtonAction:(id)sender {
    if(!commerceButton.selected)
    {
        [CommonData sharedInstance].subHomeIndex = 2;
        [self setOverScrollViewContentSize];
        [self moveToPage:2];
        commerceButton.selected = YES;
        int aKind = [[CommonData sharedInstance].userInfo[@"akind"] intValue];
        if(aKind == 1) {
            self.addButton.hidden = YES;
        }else{
            self.addButton.hidden = NO;
        }
        self.overScrollView.contentOffset = CGPointMake(0, 0);

//        [homeCommerceVC getProductFromServer:[CommonData sharedInstance].choiceCommerceCity
//                                 PleixingIds:[CommonData sharedInstance].choiceCommerceIds
//                                       Start:@""
//                                      Length:@""
//                                     Keyword:[CommonData sharedInstance].searchProductText];
    }
}

- (IBAction)itemButtonAction:(id)sender {
    if(!itemButton.selected)
    {
        [CommonData sharedInstance].subHomeIndex = 3;
        [self setOverScrollViewContentSize];
        [self moveToPage:3];
        itemButton.selected = YES;
        self.addButton.hidden = NO;
        self.overScrollView.contentOffset = CGPointMake(0, 0);
//        [homeItemVC getItemFromServer:[CommonData sharedInstance].choiceItemCity
//                                AKind:[CommonData sharedInstance].choiceItemKind
//                            FenleiIds:[CommonData sharedInstance].choiceItemIds
//                                Start:@""
//                               Length:@""
//                              Keyword:[CommonData sharedInstance].searchItemText];
    }
}

- (IBAction)serviceButtonAction:(id)sender {
    if(!serviceButton.selected)
    {
        [CommonData sharedInstance].subHomeIndex = 4;
        [self setOverScrollViewContentSize];
        [self moveToPage:4];
        serviceButton.selected = YES;
        self.addButton.hidden = NO;
        self.overScrollView.contentOffset = CGPointMake(0, 0);
//        [homeServiceVC getServiceFromServer:[CommonData sharedInstance].choiceServiceCity
//                                      AKind:[CommonData sharedInstance].choiceServiceKind
//                                  FenleiIds:[CommonData sharedInstance].choiceServiceIds
//                                      Start:@""
//                                     Length:@""
//                                    Keyword:[CommonData sharedInstance].searchServiceText];
    }
}

- (IBAction)sortButtonAction:(id)sender {
    if (!self.sortHomeButton.selected) {
        self.sortHomeButton.selected = YES;
        self.sortView.hidden = NO;
        NSInteger nCurrentPage = [CommonData sharedInstance].subHomeIndex;
        switch(nCurrentPage) {
            case 0:
                [CommonData sharedInstance].sortOrderIndex = homeFamiliarVC.currentSortOrderIndex;
                break;
            case 1:
                [CommonData sharedInstance].sortOrderIndex = homeEnterpriseVC.currentSortOrderIndex;
                break;
            case 2:
                [CommonData sharedInstance].sortOrderIndex = homeCommerceVC.currentSortOrderIndex;
                break;
            case 3:
                [CommonData sharedInstance].sortOrderIndex = homeItemVC.currentSortOrderIndex;
                break;
            case 4:
                [CommonData sharedInstance].sortOrderIndex = homeServiceVC.currentSortOrderIndex;
                break;
            default:
                break;
        }
        [self.sortView addSubview:homeSortVC.view];
        [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_TRANS_TAB_NOTIFICATION object:nil];
        [UIView animateWithDuration:0.2f animations:^{
            [homeSortVC.view setFrame:CGRectMake(0, 0, homeSortVC.view.frame.size.width, homeSortVC.view.frame.size.height)];
        } completion:^(BOOL finished) {
        }];
    }else{
        self.sortHomeButton.selected = NO;
        self.sortView.hidden = YES;
        [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_TRANS_TAB_NOTIFICATION object:nil];
        [UIView animateWithDuration:0.2f animations:^{
            [homeSortVC.view setFrame:CGRectMake(homeSortVC.view.frame.size.width, 0, homeSortVC.view.frame.size.width, homeSortVC.view.frame.size.height)];
        } completion:^(BOOL finished) {
            [homeSortVC.view removeFromSuperview];
        }];
    }
}

- (IBAction)choiceButtonAction:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMECHOICE_VIEW_NOTIFICATION object:nil];
}

- (IBAction)onShowSearchAction:(id)sender {
    if (self.sortHomeButton.selected) {
        [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_SORT_VIEW_NOTIFICATION object:nil];
    }
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_CATEGORYSEARCH_VIEW_NOTIFICATION object:nil];
}

-(IBAction)scrollViewTapGesture:(UITapGestureRecognizer *)recognizer {
    NSInteger nCurrentPage = slideHomePageCtrl.currentPage;
    NSDictionary *carouselDic = (NSDictionary *)[carouselObjectArray objectAtIndex:nCurrentPage];
    if([carouselDic[@"videoUrl"] isEqualToString:@""])
        return;
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_PROFILE_VIEW_NOTIFICATION object:carouselDic];
}

- (IBAction)showNotificationView:(id)sender {
    if (self.sortHomeButton.selected) {
        [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_SORT_VIEW_NOTIFICATION object:nil];
    }
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_NOTIFICATION_VIEW_NOTIFICATION object:nil];
}

- (void)moveToPage:(NSInteger)page {
    [self setNoSelectedAllButtons];
    CGRect frame = homeScrollView.frame;
    frame.origin.x = frame.size.width * page;
    frame.origin.y = 0;
    [homeScrollView setContentOffset:frame.origin animated:YES];
}

- (void)hideSortView:(NSNotification *)notification {
    self.sortHomeButton.selected = NO;
    self.sortView.hidden = YES;
    [self updateSortData];
   
        [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_TRANS_TAB_NOTIFICATION object:nil];
        [UIView animateWithDuration:0.2f animations:^{
            [homeSortVC.view setFrame:CGRectMake(homeSortVC.view.frame.size.width, 0, homeSortVC.view.frame.size.width, homeSortVC.view.frame.size.height)];
        } completion:^(BOOL finished) {
            //[homeSortVC.view removeFromSuperview];
        }];
}

- (void)updateSortData {
    NSInteger nCurrentPage = [CommonData sharedInstance].subHomeIndex;
    switch(nCurrentPage) {
        case 0:
        {
            homeFamiliarVC.currentSortOrderIndex = [CommonData sharedInstance].sortOrderIndex;
            [homeFamiliarVC refreshTopItems];
        }
            break;
        case 1:
        {
            homeEnterpriseVC.currentSortOrderIndex = [CommonData sharedInstance].sortOrderIndex;
            [homeEnterpriseVC refreshTopItems];
        }
            break;
        case 2:
        {
            homeCommerceVC.currentSortOrderIndex = [CommonData sharedInstance].sortOrderIndex;
            [homeCommerceVC refreshTopItems];
        }
            break;
        case 3:
        {
            homeItemVC.currentSortOrderIndex = [CommonData sharedInstance].sortOrderIndex;
            [homeItemVC refreshTopItems];
        }
            break;
        case 4:
        {
            homeServiceVC.currentSortOrderIndex = [CommonData sharedInstance].sortOrderIndex;
            [homeServiceVC refreshTopItems];
        }
            break;
        default:
            break;
    }
    
}

- (void)showResultView:(NSNotification *)notification {
    NSInteger nCurrentPage = [CommonData sharedInstance].subHomeIndex;
    switch(nCurrentPage) {
        case 0:
        {
            [homeFamiliarVC refreshTopItems];
            [self moveToPage:0];
            familiarButton.selected = YES;
        }
            break;
        case 1:
        {
            [homeEnterpriseVC refreshTopItems];
            [self moveToPage:1];
            enterpriseButton.selected = YES;
        }
            break;
        case 2:
        {
            [homeCommerceVC refreshTopItems];
            [self moveToPage:2];
            commerceButton.selected = YES;
        }
            break;
        case 3:
        {
            [homeItemVC refreshTopItems];
            [self moveToPage:3];
            itemButton.selected = YES;
        }
            break;
        case 4:
        {
            [homeServiceVC refreshTopItems];
            [self moveToPage:4];
            serviceButton.selected = YES;
        }
            break;
        case 5: // Code Search
        {
            [homeFamiliarVC refreshTopItems];
            [self moveToPage:0];
            familiarButton.selected = YES;
        }
            break;
        default:
            break;
    }
}

#pragma mark - BaseDelegate Method
- (void)finishedLoadingData:(NSInteger)loadingPage {
    switch (loadingPage) {
        case 0:
        {
            familiarTableViewHeight = homeFamiliarVC.view.frame.size.height;
            if(loadingPage == [CommonData sharedInstance].subHomeIndex){
                [self setOverScrollViewContentSize];
            }
        }
            break;
        case 1:
            enterpriseTableViewHeight = homeEnterpriseVC.view.frame.size.height;
            if(loadingPage == [CommonData sharedInstance].subHomeIndex){
                [self setOverScrollViewContentSize];
            }
            break;
        case 2:
            commerceTableViewHeight = homeCommerceVC.view.frame.size.height;
            if(loadingPage == [CommonData sharedInstance].subHomeIndex){
                [self setOverScrollViewContentSize];
            }
            break;
        case 3:
            itemTableViewHeight = homeItemVC.view.frame.size.height;
            if(loadingPage == [CommonData sharedInstance].subHomeIndex){
                [self setOverScrollViewContentSize];
            }
            break;
        case 4:
            serviceTableViewHeight =homeServiceVC.view.frame.size.height;
            if(loadingPage == [CommonData sharedInstance].subHomeIndex){
                [self setOverScrollViewContentSize];
            }
            break;
        case 5:
            familiarTableViewHeight = homeFamiliarVC.view.frame.size.height;
            if(loadingPage == [CommonData sharedInstance].subHomeIndex){
                [self setOverScrollViewContentSize];
            }
            break;
        default:
            break;
    }
}

- (void)stopLoadingIndicator {
    [topRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
    [bottomRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
}

- (void)setOverScrollViewContentSize {
    switch ([CommonData sharedInstance].subHomeIndex) {
        case 0:
            if(familiarTableViewHeight < overScrollView.frame.size.height - slideHomeScrollView.frame.size.height) {
                if(familiarTableViewHeight == 0) {
                    self.blankView.hidden = NO;
                }else{
                    self.blankView.hidden = YES;
                }
                [overScrollView setContentSize:CGSizeMake(overScrollView.frame.size.width, overScrollView.frame.size.height - slideHomeScrollView.frame.size.height)];
            }else{
                self.blankView.hidden = YES;
                [overScrollView setContentSize:CGSizeMake(overScrollView.frame.size.width, familiarTableViewHeight)];
            }
            break;
        case 1:
            if(enterpriseTableViewHeight < overScrollView.frame.size.height - slideHomeScrollView.frame.size.height) {
                if(enterpriseTableViewHeight == 0) {
                    self.blankView.hidden = NO;
                }else{
                    self.blankView.hidden = YES;
                }
                [overScrollView setContentSize:CGSizeMake(overScrollView.frame.size.width, overScrollView.frame.size.height - slideHomeScrollView.frame.size.height)];
            }else{
                self.blankView.hidden = YES;
                [overScrollView setContentSize:CGSizeMake(overScrollView.frame.size.width, enterpriseTableViewHeight)];
            }
            break;
        case 2:
            if(commerceTableViewHeight < overScrollView.frame.size.height - slideHomeScrollView.frame.size.height) {
                if(commerceTableViewHeight == 0) {
                    self.blankView.hidden = NO;
                }else{
                    self.blankView.hidden = YES;
                }
                [overScrollView setContentSize:CGSizeMake(overScrollView.frame.size.width, overScrollView.frame.size.height - slideHomeScrollView.frame.size.height)];
            }else{
                self.blankView.hidden = YES;
                [overScrollView setContentSize:CGSizeMake(overScrollView.frame.size.width, commerceTableViewHeight + slideHomeScrollView.frame.size.height)];
            }
            break;
        case 3:
            if(itemTableViewHeight < overScrollView.frame.size.height - slideHomeScrollView.frame.size.height) {
                if(itemTableViewHeight == 0) {
                    self.blankView.hidden = NO;
                }else{
                    self.blankView.hidden = YES;
                }
                [overScrollView setContentSize:CGSizeMake(overScrollView.frame.size.width, overScrollView.frame.size.height - slideHomeScrollView.frame.size.height)];
            }else{
                self.blankView.hidden = YES;
                [overScrollView setContentSize:CGSizeMake(overScrollView.frame.size.width, itemTableViewHeight)];
            }
            break;
        case 4:
            if(serviceTableViewHeight < overScrollView.frame.size.height - slideHomeScrollView.frame.size.height) {
                if(serviceTableViewHeight == 0) {
                    self.blankView.hidden = NO;
                }else{
                    self.blankView.hidden = YES;
                }
                [overScrollView setContentSize:CGSizeMake(overScrollView.frame.size.width, overScrollView.frame.size.height - slideHomeScrollView.frame.size.height)];
            }else{
                self.blankView.hidden = YES;
                [overScrollView setContentSize:CGSizeMake(overScrollView.frame.size.width, serviceTableViewHeight)];
            }
            break;
        default:
            break;
    }
    self.homeScrollViewHeightConstraint.constant = overScrollView.contentSize.height;
}

- (IBAction)onAddAction:(id)sender {
    switch ([CommonData sharedInstance].subHomeIndex) {
        case 2:
            [homeCommerceVC addAction];
            break;
        case 3:
            [homeItemVC addAction];
            break;
        case 4:
            [homeServiceVC addAction];
            break;
            
        default:
            break;
    }
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
