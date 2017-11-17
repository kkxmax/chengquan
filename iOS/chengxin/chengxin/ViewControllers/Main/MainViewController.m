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
#import "HomeChoiceViewController.h"
//#import "HomeItemDetailViewController.h"

@interface MainViewController ()
{
    UIViewController *currentVC;
    UINavigationController *choiceNavVC;
    HomeChoiceViewController *choiceVC;
}
@end

@implementation MainViewController
@synthesize mainView, mainTabBar, transTabBarView, homeChoiceBackgroundView, homeChoiceView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [mainTabBar setSelectedItem:mainTabBar.items[0]];
    
    // NSNotification for HomeSortView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(activityTransTabBar:) name:ACTIVITY_TRANS_TAB_NOTIFICATION object:nil];
    // NSNotification for HomeChoiceView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showHomeChoiceView:) name:SHOW_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(hideHomeChoiceView:) name:HIDE_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    // NSNotification for HomeItemDetailView
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showHomeItemDetailView:) name:SHOW_HOMEITEMDETAIL_VIEW_NOTIFICATION object:nil];

    // HomeChoiceViewController
    choiceNavVC = [[UINavigationController alloc] init];
    choiceVC = [[HomeChoiceViewController alloc] initWithNibName:@"HomeChoiceViewController" bundle:nil];
    [choiceNavVC pushViewController:choiceVC animated:NO];
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
        [transTabBarView setFrame:CGRectMake(transTabBarView.frame.size.width, transTabBarView.frame.origin.y, transTabBarView.frame.size.width, transTabBarView.frame.size.height)];
        transTabBarView.hidden = NO;
        [UIView animateWithDuration:0.5f animations:^{
            [transTabBarView setFrame:CGRectMake(0, transTabBarView.frame.origin.y, transTabBarView.frame.size.width, transTabBarView.frame.size.height)];
        } completion:^(BOOL finished) {
        }];
    }else{
        [UIView animateWithDuration:0.5f animations:^{
            [transTabBarView setFrame:CGRectMake(transTabBarView.frame.size.width, transTabBarView.frame.origin.y, transTabBarView.frame.size.width, transTabBarView.frame.size.height)];
        } completion:^(BOOL finished) {
            transTabBarView.hidden = YES;
        }];
    }
}

- (void)showHomeChoiceView:(NSNotification *) notification {
    [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
   
    [choiceVC.view setFrame:homeChoiceView.bounds];
    [choiceNavVC.view setFrame:homeChoiceView.bounds];
    [homeChoiceView addSubview:choiceNavVC.view];
    homeChoiceBackgroundView.hidden = NO;
    [UIView animateWithDuration:0.5f animations:^{
        [homeChoiceBackgroundView setFrame:CGRectMake(0, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    }];
}

- (void)hideHomeChoiceView:(NSNotification *) notification {
    [UIView animateWithDuration:0.5f animations:^{
        [homeChoiceBackgroundView setFrame:CGRectMake(self.view.frame.size.width, homeChoiceBackgroundView.frame.origin.y, homeChoiceBackgroundView.frame.size.width, homeChoiceBackgroundView.frame.size.height)];
    } completion:^(BOOL finished) {
        homeChoiceBackgroundView.hidden = YES;
        [choiceNavVC.view removeFromSuperview];
    }];
}

- (void)showHomeItemDetailView:(NSNotification *) notification {
    NSIndexPath *itemDetailIndexPath = (NSIndexPath *)notification.object;
    NSLog(@"%ld", (long)itemDetailIndexPath.row);
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
