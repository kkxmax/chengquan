//
//  MainViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "MainViewController.h"
#import "HomeViewController.h"
#import "HotViewController.h"
#import "EvaluateViewController.h"
#import "FavouriteViewController.h"
#import "Global.h"
#import "MineViewController.h"
#import "ChengXinRecordViewController.h"
#import "HomePersonalChoiceViewController.h"

#import "ChengXinReportViewController.h"

#import "HomeFamiliarDetailViewController.h"
#import "HomeEnterpriseDetailViewController.h"
#import "HomeCommerceDetailViewController.h"

#import "HomeItemDetailViewController.h"
#import "SearchProductsViewController.h"
#import "FavouritesTableViewController.h"
#import "FavouriteViewController.h"
#import "MineViewController.h"
#import "HomeItemAddViewController.h"
#import "CategorySearchViewController.h"
#import "ProfileViewController.h"
#import "EvaluateDetailViewController.h"
#import "SystemInfoViewController.h"

#import "HomeOfficeChoiceViewController.h"
#import "HomeCommerceChoiceViewController.h"
#import "HomeItemChoiceViewController.h"
#import "HomeServiceChoiceViewController.h"
#import "HomeCommerceAddViewController.h"

@interface MainViewController ()
{
    UIViewController *currentVC;
    UINavigationController *choiceNavVC;
    UIViewController *choiceVC;
}
@end

@implementation MainViewController
@synthesize mainView, mainTabBar, transTabBarView, homeChoiceBackgroundView, homeChoiceView, homeChoiceTransView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [mainTabBar setSelectedItem:mainTabBar.items[0]];
    
    // NSNotification for HomeSortView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(activityTransTabBar:) name:ACTIVITY_TRANS_TAB_NOTIFICATION object:nil];
    // NSNotification for HomeChoiceView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showHomeChoiceView:) name:SHOW_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(hideHomeChoiceView:) name:HIDE_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    // NSNotification for HomeFamiliarDetailView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showHomeFamilarDetailView:) name:SHOW_HOMEFAMILIARDETAIL_VIEW_NOTIFICATION object:nil];
    // NSNotification for HomeEnterpriseDetailView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showHomeEnterpriseDetailView:) name:SHOW_HOMEENTERPRISEDETAIL_VIEW_NOTIFICATION object:nil];
    // NSNotification for HomeCommerceDetailView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showHomeCommerceDetailView:) name:SHOW_HOMECOMMERCEDETAIL_VIEW_NOTIFICATION object:nil];
    // NSNotification for HomeItemDetailView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showHomeItemDetailView:) name:SHOW_HOMEITEMDETAIL_VIEW_NOTIFICATION object:nil];
    // NSNotification for HomeServiceDetailView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showHomeServiceDetailView:) name:SHOW_HOMESERVICEDETAIL_VIEW_NOTIFICATION object:nil];
    // NSNotification for HomeItemAddView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showHomeItemAddView:) name:SHOW_HOMEITEMADD_VIEW_NOTIFICATION object:nil];
    // NSNotification for CategorySearchView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showCategorySearchView:) name:SHOW_CATEGORYSEARCH_VIEW_NOTIFICATION object:nil];
    // NSNotification for ProfileView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showProfileView:) name:SHOW_PROFILE_VIEW_NOTIFICATION object:nil];
    // NSNotification for EvaluteDetailView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showEvaluateDetailView:) name:SHOW_EVALUTEDETAIL_VIEW_NOTIFICATION object:nil];
    // NSNotification for EvaluteDetailView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showHomeCommerceAddView:) name:SHOW_HOMEPRODUCTADD_VIEW_NOTIFICATION object:nil];
    // NSNotification for NotificationView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showNotificationView:) name:SHOW_NOTIFICATION_VIEW_NOTIFICATION object:nil];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    if(!currentVC) {
        currentVC = [[HomeViewController alloc] initWithNibName:@"HomeViewController" bundle:nil];
    }
    else {
        [self closeAllViews];
    }
    [self showView];
    
}

- (void)closeAllViews {
    if(currentVC.view)
        [currentVC.view removeFromSuperview];
    if(currentVC)
        [currentVC removeFromParentViewController];
}

- (void)showView {
    [currentVC.view setFrame:CGRectMake(0, 0, mainView.frame.size.width, mainView.frame.size.height)];
    [mainView addSubview:currentVC.view];
    [self addChildViewController:currentVC];
}

#pragma mark - NSNotification
- (void)activityTransTabBar:(NSNotification *) notification
{
    if(transTabBarView.isHidden) {
        transTabBarView.hidden = NO;
    }else{
        transTabBarView.hidden = YES;
    }
}

- (void)showHomeChoiceView:(NSNotification *) notification {
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
   
    // HomeChoiceViewController
    choiceNavVC = [[UINavigationController alloc] init];
    NSInteger index = [CommonData sharedInstance].subHomeIndex;
    if (index == SUB_HOME_PERSONAL)
        choiceVC = [[HomePersonalChoiceViewController alloc] initWithNibName:@"HomePersonalChoiceViewController" bundle:nil];
    else if(index == SUB_HOME_ENTERPRISE)
        choiceVC = [[HomeOfficeChoiceViewController alloc] initWithNibName:@"HomeOfficeChoiceViewController" bundle:nil];
    else if(index == SUB_HOME_COMMERCE)
        choiceVC = [[HomeCommerceChoiceViewController alloc] initWithNibName:@"HomeCommerceChoiceViewController" bundle:nil];
    else if(index == SUB_HOME_ITEM)
        choiceVC = [[HomeItemChoiceViewController alloc] initWithNibName:@"HomeItemChoiceViewController" bundle:nil];
    else if(index == SUB_HOME_SERVICE)
        choiceVC = [[HomeServiceChoiceViewController alloc] initWithNibName:@"HomeServiceChoiceViewController" bundle:nil];
    
    [choiceNavVC popViewControllerAnimated:NO];
    [choiceNavVC pushViewController:choiceVC animated:NO];
    
    [choiceVC.view setFrame:homeChoiceView.bounds];
    [choiceNavVC.view setFrame:homeChoiceView.bounds];
    [homeChoiceView addSubview:choiceNavVC.view];
    homeChoiceBackgroundView.hidden = NO;
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    [UIView animateWithDuration:0.5f animations:^{
        [homeChoiceBackgroundView setFrame:CGRectMake(0, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    } completion:^(BOOL finished) {
        homeChoiceTransView.hidden = NO;
    }];
}

- (void)hideHomeChoiceView:(NSNotification *) notification {
    homeChoiceTransView.hidden = YES;
    [UIView animateWithDuration:0.5f animations:^{
        [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    } completion:^(BOOL finished) {
        homeChoiceBackgroundView.hidden = YES;
        [choiceNavVC.view removeFromSuperview];
    }];
    
}

- (void)showHomeFamilarDetailView:(NSNotification *) notification {
    NSIndexPath *itemDetailIndexPath = (NSIndexPath *)notification.object;
    NSLog(@"%ld", (long)itemDetailIndexPath.row);
    HomeFamiliarDetailViewController *homeItemDetailVC = [[HomeFamiliarDetailViewController alloc] initWithNibName:@"HomeFamiliarDetailViewController" bundle:nil];
    [self.navigationController pushViewController:homeItemDetailVC animated:YES];
}

- (void)showHomeEnterpriseDetailView:(NSNotification *) notification {
    NSIndexPath *itemDetailIndexPath = (NSIndexPath *)notification.object;
    NSLog(@"%ld", (long)itemDetailIndexPath.row);
    HomeEnterpriseDetailViewController *homeItemDetailVC = [[HomeEnterpriseDetailViewController alloc] initWithNibName:@"HomeEnterpriseDetailViewController" bundle:nil];
    [self.navigationController pushViewController:homeItemDetailVC animated:YES];
}


- (void)showHomeCommerceDetailView:(NSNotification *) notification {
    NSIndexPath *itemDetailIndexPath = (NSIndexPath *)notification.object;
    NSLog(@"%ld", (long)itemDetailIndexPath.row);
    HomeCommerceDetailViewController *homeItemDetailVC = [[HomeCommerceDetailViewController alloc] initWithNibName:@"HomeCommerceDetailViewController" bundle:nil];
    [self.navigationController pushViewController:homeItemDetailVC animated:YES];
}

- (void)showHomeCommerceAddView:(NSNotification *) notification {
    HomeCommerceAddViewController *homeCommerceAddVC = [[HomeCommerceAddViewController alloc] initWithNibName:@"HomeCommerceAddViewController" bundle:nil];
    [self.navigationController pushViewController:homeCommerceAddVC animated:YES];
}

- (void)showHomeItemDetailView:(NSNotification *) notification {
    NSIndexPath *itemDetailIndexPath = (NSIndexPath *)notification.object;
    NSLog(@"%ld", (long)itemDetailIndexPath.row);
    HomeItemDetailViewController *homeItemDetailVC = [[HomeItemDetailViewController alloc] initWithNibName:@"HomeItemDetailViewController" bundle:nil];
    [self.navigationController pushViewController:homeItemDetailVC animated:YES];
}

- (void)showHomeServiceDetailView:(NSNotification *) notification {
    NSIndexPath *itemDetailIndexPath = (NSIndexPath *)notification.object;
    NSLog(@"%ld", (long)itemDetailIndexPath.row);
    HomeItemDetailViewController *homeItemDetailVC = [[HomeItemDetailViewController alloc] initWithNibName:@"HomeItemDetailViewController" bundle:nil];
    [self.navigationController pushViewController:homeItemDetailVC animated:YES];
}

- (void)showHomeItemAddView:(NSNotification *) notification {
    HomeItemAddViewController *homeItemAddVC = [[HomeItemAddViewController alloc] initWithNibName:@"HomeItemAddViewController" bundle:nil];
    [self.navigationController pushViewController:homeItemAddVC animated:YES];
}

- (void)showCategorySearchView:(NSNotification *) notification {
    CategorySearchViewController *categorySearchVC = [[CategorySearchViewController alloc] initWithNibName:@"CategorySearchViewController" bundle:nil];
    [self.navigationController pushViewController:categorySearchVC animated:YES];
}

- (void)showProfileView:(NSNotification *) notification {
    NSString *selectedName = (NSString *)notification.object;
    ProfileViewController *profileVC = [[ProfileViewController alloc] initWithNibName:@"ProfileViewController" bundle:nil];
    profileVC.selectedName =  selectedName;
    profileVC.selectedImage = [UIImage imageNamed:[NSString stringWithFormat:@"%@.png", selectedName]];
    [self.navigationController pushViewController:profileVC animated:YES];
}

- (void)showEvaluateDetailView:(NSNotification *) notification {
    NSIndexPath *itemDetailIndexPath = (NSIndexPath *)notification.object;
    NSLog(@"%ld", (long)itemDetailIndexPath.row);
    EvaluateDetailViewController *evalDetailVC = [[EvaluateDetailViewController alloc] initWithNibName:@"EvaluateDetailViewController" bundle:nil];
    [self.navigationController pushViewController:evalDetailVC animated:YES];
}

- (void)showNotificationView:(NSNotification *)notification {
    SystemInfoViewController *systemInfoVC = [[SystemInfoViewController alloc] initWithNibName:@"SystemInfoViewController" bundle:nil];
    [self.navigationController pushViewController:systemInfoVC animated:YES];
}

#pragma mark - Action
- (IBAction)handleSwipe:(UISwipeGestureRecognizer *)recognizer
{
    if(recognizer.direction == UISwipeGestureRecognizerDirectionRight && !homeChoiceView.isHidden)
    {
        [self hideHomeChoiceView:nil];
    }
}

#pragma mark - UITabbar delegate
-(void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item {
    [self closeAllViews];
    switch (item.tag) {
        case 0:
        {
            currentVC = [[HomeViewController alloc] initWithNibName:@"HomeViewController" bundle:nil];
        }
            break;
        case 1:
        {
            currentVC = [[HotViewController alloc] initWithNibName:@"HotViewController" bundle:nil];
        }
            break;
        case 2:
        {
            currentVC = [[EvaluateViewController alloc] initWithNibName:@"EvaluateViewController" bundle:nil];
        }
            break;
        case 3:
        {
            currentVC = [[FavouriteViewController alloc] initWithNibName:@"FavouriteViewController" bundle:nil];
        }
            break;
        case 4:
        {
            currentVC = [[MineViewController alloc] initWithNibName:@"MineViewController" bundle:nil];
        }
            break;
        default:
            break;
    }
    [self showView];
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
