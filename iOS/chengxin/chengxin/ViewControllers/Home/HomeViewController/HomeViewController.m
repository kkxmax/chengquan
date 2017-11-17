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
#import "CarouselObject.h"
#import "UIImageView+WebCache.h"

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
}
@end

@implementation HomeViewController


@synthesize searchBar, messageNumberLabel, homeScrollView, familiarButton, commerceButton, itemButton, serviceButton, enterpriseButton, overScrollView, slideHomeScrollView, slideHomePageCtrl, indicatorView;

- (void)viewDidLoad {
    [super viewDidLoad];
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
    [CommonData sharedInstance].choiceXyleixingIds = @"";
    
    [self setNoSelectedAllButtons];
    familiarButton.selected = YES;
    // Get Web Data
    [self getCarouselList];

    [self updateNotification];
    appDelegate.notificationDelegate = self;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
    // Customize Search Bar
    [searchBar setImage:[UIImage imageNamed:@"nav_search"] forSearchBarIcon:UISearchBarIconSearch state:UIControlStateNormal];
    [searchBar setBackgroundImage:[UIImage imageNamed:@"nav_bg"]];
    
    // Customize message number label
    messageNumberLabel.layer.cornerRadius = messageNumberLabel.frame.size.width / 2;
    messageNumberLabel.layer.masksToBounds = YES;
    
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // InitializePageView
    [self initPageView];
}

- (void)reloadHomeData {
    NSInteger index = [CommonData sharedInstance].subHomeIndex;
    if (index == SUB_HOME_PERSONAL) {
        [homeFamiliarVC getFriendFromServer:[CommonData sharedInstance].choiceCity
                                      aKind:[CommonData sharedInstance].choiceAkind
                               XyleixingIds:[CommonData sharedInstance].choiceXyleixingIds
                                      Start:@""
                                     Length:@""
                                    Keyword:[CommonData sharedInstance].searchFamiliarText];
    }
    else if (index == SUB_HOME_ENTERPRISE) {
        [homeEnterpriseVC getEnterFromServer:[CommonData sharedInstance].choiceCity
                                   EnterKind:[CommonData sharedInstance].choiceEnterKind
                                XyleixingIds:[CommonData sharedInstance].choiceXyleixingIds
                                       Start:@""
                                      Length:@""
                                     Keyword:[CommonData sharedInstance].searchEnterpriseText];
    }
    else if (index == SUB_HOME_COMMERCE) {
        [homeCommerceVC getProductFromServer:[CommonData sharedInstance].choiceCity
                                 PleixingIds:[CommonData sharedInstance].choicePleixingIds
                                       Start:@""
                                      Length:@""
                                     Keyword:[CommonData sharedInstance].searchProductText];

    }
    else if (index == SUB_HOME_ITEM) {
        [homeItemVC getItemFromServer:[CommonData sharedInstance].choiceCity
                                AKind:[CommonData sharedInstance].choiceAkind
                            FenleiIds:[CommonData sharedInstance].choiceFenleiIds
                                Start:@""
                               Length:@""
                              Keyword:[CommonData sharedInstance].searchItemText];
    }
    else if (index == SUB_HOME_SERVICE) {
        [homeServiceVC getServiceFromServer:[CommonData sharedInstance].choiceCity
                                      AKind:[CommonData sharedInstance].choiceAkind
                                  FenleiIds:[CommonData sharedInstance].choiceFenleiIds
                                      Start:@""
                                     Length:@""
                                    Keyword:[CommonData sharedInstance].searchServiceText];

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
    indicatorView.hidden = NO;
    [indicatorView startAnimating];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getCarouselList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];

    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETCAROUSELLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                carouselObjectArray = [[NSMutableArray alloc] init];
                NSMutableArray *carouselMutableArray = (NSMutableArray *)(dicRes[@"data"]);
                for (int i = 0; i < carouselMutableArray.count; i++) {
                    CarouselObject *carouselObj = [[CarouselObject alloc] init];
                    NSDictionary *carouselDic = carouselMutableArray[i];
                    carouselObj.objectID = carouselDic[@"id"];
                    carouselObj.imageTitle = carouselDic[@"imgName"];
                    carouselObj.imageUrl = carouselDic[@"imgUrl"];
                    carouselObj.videoTitle = carouselDic[@"videoTitle"];
                    carouselObj.videoUrl = carouselDic[@"videoUrl"];
                    [carouselObjectArray addObject:carouselObj];
                }
                
                // Set slide page time
                [indicatorView stopAnimating];
                [self showSlideShow];
                [homeFamiliarVC getFriendFromServer:@""
                                              aKind:@""
                                       XyleixingIds:@""
                                              Start:@""
                                             Length:@""
                                            Keyword:[CommonData sharedInstance].searchFamiliarText];
            }
        }
    }];
}
#pragma mark - Page Slide
- (void)showSlideShow {
    slideHomeScrollView.contentSize = CGSizeMake(slideHomeScrollView.frame.size.width * carouselObjectArray.count, slideHomeScrollView.frame.size.height);
    ViewSize = slideHomeScrollView.bounds;
    nImageNum = 0;
    while (nImageNum < carouselObjectArray.count) {
        CarouselObject *carouselObj = [carouselObjectArray objectAtIndex:nImageNum];
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:ViewSize];
        imageView.contentMode = UIViewContentModeScaleToFill;
        if(carouselObj.imageUrl) {
            [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, carouselObj.imageUrl]]
                                completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
                                }];
        }
        [slideHomeScrollView addSubview:imageView];
        ViewSize = CGRectOffset(ViewSize, slideHomeScrollView.bounds.size.width, 0);
        nImageNum ++;
    }
    slideHomePageTimer = [NSTimer scheduledTimerWithTimeInterval:SLIDE_SECOND target:self selector:@selector(PageMove) userInfo:nil repeats:YES];
    [GeneralUtil hideProgress];
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
   
    [homeFamiliarVC.view setFrame:CGRectMake(0, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeFamiliarVC.view];
    
    [homeEnterpriseVC.view setFrame:CGRectMake(homeScrollView.frame.size.width, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeEnterpriseVC.view];
    
    [homeCommerceVC.view setFrame:CGRectMake(homeScrollView.frame.size.width * 2, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeCommerceVC.view];
    
    [homeItemVC.view setFrame:CGRectMake(homeScrollView.frame.size.width * 3, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeItemVC.view];
    
    [homeServiceVC.view setFrame:CGRectMake(homeScrollView.frame.size.width * 4, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeServiceVC.view];
    
    [homeSortVC.view setFrame:CGRectMake(overScrollView.frame.size.width * 1, 0, overScrollView.frame.size.width, overScrollView.frame.size.height)];
    [homeScrollView setContentSize:CGSizeMake(homeScrollView.frame.size.width * 5, homeScrollView.frame.size.height)];
}

- (IBAction)swipeLeftHomeScrollView:(UISwipeGestureRecognizer *)gestureRecognizer {
    [slideHomePageTimer invalidate];
    slideHomePageTimer = nil;
    CGFloat pageWidth = slideHomeScrollView.frame.size.width;
    float fractionalPage = slideHomeScrollView.contentOffset.x / pageWidth;
    NSInteger nCurrentPage = lround(fractionalPage);
    if(nCurrentPage < 4) {
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
#pragma mark - UIScrollViewDelegate
- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    CGFloat pageWidth = scrollView.frame.size.width;
    float fractionalPage = scrollView.contentOffset.x / pageWidth;
    NSInteger page = lround(fractionalPage);
    if (previousPage != page) {
            [self setNoSelectedAllButtons];
            [CommonData sharedInstance].subHomeIndex = page;
            previousPage = page;
            switch (page) {
                case 0:
                {
                    familiarButton.selected = YES;
                    [CommonData sharedInstance].choiceCity = @"";
                    [CommonData sharedInstance].choiceAkind = @"";
                    [CommonData sharedInstance].choiceXyleixingIds = @"";
                    [homeFamiliarVC getFriendFromServer:@""
                                                  aKind:@""
                                           XyleixingIds:@""
                                                  Start:@""
                                                 Length:@""
                                                Keyword:[CommonData sharedInstance].searchFamiliarText];
                }
                    break;
                case 1:
                {
                    enterpriseButton.selected = YES;
                    [CommonData sharedInstance].choiceCity = @"";
                    [CommonData sharedInstance].choiceEnterKind = @"";
                    [CommonData sharedInstance].choiceXyleixingIds = @"";
                    
                    [homeEnterpriseVC getEnterFromServer:@""
                                               EnterKind:@""
                                            XyleixingIds:@""
                                                   Start:@""
                                                  Length:@""
                                                 Keyword:[CommonData sharedInstance].searchEnterpriseText];
                }
                    break;
                case 2:
                {
                    commerceButton.selected = YES;
                    [CommonData sharedInstance].choiceCity = @"";
                    [CommonData sharedInstance].choicePleixingIds = @"";
                    
                    [homeCommerceVC getProductFromServer:@""
                                             PleixingIds:@""
                                                   Start:@""
                                                  Length:@""
                                                 Keyword:[CommonData sharedInstance].searchProductText];
                }
                    break;
                case 3:
                {
                    itemButton.selected = YES;
                    [CommonData sharedInstance].choiceCity = @"";
                    [CommonData sharedInstance].choiceAkind = @"";
                    [CommonData sharedInstance].choiceFenleiIds = @"";
                    
                    [homeItemVC getItemFromServer:@""
                                            AKind:@""
                                        FenleiIds:@""
                                            Start:@""
                                           Length:@""
                                          Keyword:[CommonData sharedInstance].searchItemText];
                }
                    break;
                case 4:
                {
                    serviceButton.selected = YES;
                    [CommonData sharedInstance].choiceCity = @"";
                    [CommonData sharedInstance].choiceAkind = @"";
                    [CommonData sharedInstance].choiceFenleiIds = @"";
                    
                    [homeServiceVC getServiceFromServer:@""
                                                  AKind:@""
                                              FenleiIds:@""
                                                  Start:@""
                                                 Length:@""
                                                Keyword:[CommonData sharedInstance].searchServiceText];
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
        [CommonData sharedInstance].choiceCity = @"";
        [CommonData sharedInstance].choiceAkind = @"";
        [CommonData sharedInstance].choiceXyleixingIds = @"";
        [self moveToPage:0];
        familiarButton.selected = YES;
        [CommonData sharedInstance].subHomeIndex = 0;
    }
}

- (IBAction)enterpriseButtonAction:(id)sender {
    if(!enterpriseButton.selected)
    {
        [CommonData sharedInstance].choiceCity = @"";
        [CommonData sharedInstance].choiceEnterKind = @"";
        [CommonData sharedInstance].choiceXyleixingIds = @"";
        [self moveToPage:1];
        enterpriseButton.selected = YES;
        [CommonData sharedInstance].subHomeIndex = 1;
    }
}

- (IBAction)commerceButtonAction:(id)sender {
    if(!commerceButton.selected)
    {
        [CommonData sharedInstance].choiceCity = @"";
        [CommonData sharedInstance].choicePleixingIds = @"";
        [self moveToPage:2];
        commerceButton.selected = YES;
        [CommonData sharedInstance].subHomeIndex = 2;
    }
}

- (IBAction)itemButtonAction:(id)sender {
    if(!itemButton.selected)
    {
        [CommonData sharedInstance].choiceCity = @"";
        [CommonData sharedInstance].choiceAkind = @"";
        [CommonData sharedInstance].choiceFenleiIds = @"";
        [self moveToPage:3];
        itemButton.selected = YES;
        [CommonData sharedInstance].subHomeIndex = 3;
    }
}

- (IBAction)serviceButtonAction:(id)sender {
    if(!serviceButton.selected)
    {
        [CommonData sharedInstance].choiceCity = @"";
        [CommonData sharedInstance].choiceAkind = @"";
        [CommonData sharedInstance].choiceFenleiIds = @"";
        [self moveToPage:4];
        serviceButton.selected = YES;
        [CommonData sharedInstance].subHomeIndex = 4;
    }
}

static BOOL completedSortViewAnimation = YES;
- (IBAction)sortButtonAction:(id)sender {
    if(!completedSortViewAnimation)
        return;
    completedSortViewAnimation = NO;
    UIButton *sortButton = (UIButton *)sender;
    if (!sortButton.isSelected) {
        sortButton.selected = YES;
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
        [overScrollView addSubview:homeSortVC.view];
        [UIView animateWithDuration:0.5f animations:^{
            [homeSortVC.view setFrame:CGRectMake(0, 0, homeSortVC.view.frame.size.width, homeSortVC.view.frame.size.height)];
        } completion:^(BOOL finished) {
            completedSortViewAnimation = YES;
            [[NSNotificationCenter defaultCenter] postNotificationName:ACTIVITY_TRANS_TAB_NOTIFICATION object:nil];
        }];
    }else{
        self.sortHomeButton.selected = NO;
        [[NSNotificationCenter defaultCenter] postNotificationName:ACTIVITY_TRANS_TAB_NOTIFICATION object:nil];
        [UIView animateWithDuration:0.5f animations:^{
            [homeSortVC.view setFrame:CGRectMake(homeSortVC.view.frame.size.width, 0, homeSortVC.view.frame.size.width, homeSortVC.view.frame.size.height)];
        } completion:^(BOOL finished) {
            [homeSortVC.view removeFromSuperview];
            completedSortViewAnimation = YES;
        }];
    }
}

- (IBAction)choiceButtonAction:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMECHOICE_VIEW_NOTIFICATION object:nil];
}

- (IBAction)onShowSearchAction:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_CATEGORYSEARCH_VIEW_NOTIFICATION object:nil];
}

-(IBAction)scrollViewTapGesture:(UITapGestureRecognizer *)recognizer {
    NSInteger nCurrentPage = slideHomePageCtrl.currentPage;
    NSString *imageName = [NSString stringWithFormat:@"%ld", (long)(HOME_IMAGE_START + nCurrentPage)];
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_PROFILE_VIEW_NOTIFICATION object:imageName];
}

- (IBAction)showNotificationView:(id)sender {
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
    [self updateSortData];
    [[NSNotificationCenter defaultCenter] postNotificationName:ACTIVITY_TRANS_TAB_NOTIFICATION object:nil];
    [UIView animateWithDuration:0.5f animations:^{
        [homeSortVC.view setFrame:CGRectMake(homeSortVC.view.frame.size.width, 0, homeSortVC.view.frame.size.width, homeSortVC.view.frame.size.height)];
    } completion:^(BOOL finished) {
        [homeSortVC.view removeFromSuperview];
        completedSortViewAnimation = YES;
    }];
}

- (void)updateSortData {
    NSInteger nCurrentPage = [CommonData sharedInstance].subHomeIndex;
    switch(nCurrentPage) {
        case 0:
        {
            homeFamiliarVC.currentSortOrderIndex = [CommonData sharedInstance].sortOrderIndex;
            [homeFamiliarVC getFriendFromServer:@""
                                          aKind:@""
                                   XyleixingIds:@""
                                          Start:@""
                                         Length:@""
                                        Keyword:[CommonData sharedInstance].searchFamiliarText];
        }
            break;
        case 1:
        {
            homeEnterpriseVC.currentSortOrderIndex = [CommonData sharedInstance].sortOrderIndex;
            [homeEnterpriseVC getEnterFromServer:@""
                                       EnterKind:@""
                                    XyleixingIds:@""
                                           Start:@""
                                          Length:@""
                                         Keyword:[CommonData sharedInstance].searchEnterpriseText];
        }
            break;
        case 2:
        {
            homeCommerceVC.currentSortOrderIndex = [CommonData sharedInstance].sortOrderIndex;
            [homeCommerceVC getProductFromServer:@""
                                     PleixingIds:@""
                                           Start:@""
                                          Length:@""
                                         Keyword:[CommonData sharedInstance].searchProductText];
        }
            break;
        case 3:
        {
            homeItemVC.currentSortOrderIndex = [CommonData sharedInstance].sortOrderIndex;
            [homeItemVC getItemFromServer:@""
                                    AKind:@""
                                FenleiIds:@""
                                    Start:@""
                                   Length:@""
                                  Keyword:[CommonData sharedInstance].searchItemText];
        }
            break;
        case 4:
        {
            homeServiceVC.currentSortOrderIndex = [CommonData sharedInstance].sortOrderIndex;
            [homeServiceVC getServiceFromServer:@""
                                          AKind:@""
                                      FenleiIds:@""
                                          Start:@""
                                         Length:@""
                                        Keyword:[CommonData sharedInstance].searchServiceText];
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
            [homeFamiliarVC getFriendFromServer:@""
                                          aKind:@""
                                   XyleixingIds:@""
                                          Start:@""
                                         Length:@""
                                        Keyword:[CommonData sharedInstance].searchFamiliarText];
            [self moveToPage:0];
            familiarButton.selected = YES;
        }
            break;
        case 1:
        {
            [homeEnterpriseVC getEnterFromServer:@""
                                       EnterKind:@""
                                    XyleixingIds:@""
                                           Start:@""
                                          Length:@""
                                         Keyword:[CommonData sharedInstance].searchEnterpriseText];
            [self moveToPage:1];
            enterpriseButton.selected = YES;
        }
            break;
        case 2:
        {
            [homeCommerceVC getProductFromServer:@""
                                     PleixingIds:@""
                                           Start:@""
                                          Length:@""
                                         Keyword:[CommonData sharedInstance].searchProductText];
            [self moveToPage:2];
            commerceButton.selected = YES;
        }
            break;
        case 3:
        {
            [homeItemVC getItemFromServer:@""
                                    AKind:@""
                                FenleiIds:@""
                                    Start:@""
                                   Length:@""
                                  Keyword:[CommonData sharedInstance].searchItemText];
            [self moveToPage:3];
            itemButton.selected = YES;
        }
            break;
        case 4:
        {
            [homeServiceVC getServiceFromServer:@""
                                          AKind:@""
                                      FenleiIds:@""
                                          Start:@""
                                         Length:@""
                                        Keyword:[CommonData sharedInstance].searchServiceText];
            [self moveToPage:4];
            serviceButton.selected = YES;
        }
            break;
        case 5: // Code Search
        {
            [homeFamiliarVC getFriendFromServer:@""
                                          aKind:@""
                                   XyleixingIds:@""
                                          Start:@""
                                         Length:@""
                                        Keyword:[CommonData sharedInstance].searchFamiliarText];
            [self moveToPage:0];
            familiarButton.selected = YES;
        }
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
