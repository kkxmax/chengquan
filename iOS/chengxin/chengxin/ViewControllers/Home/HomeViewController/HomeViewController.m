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

@interface HomeViewController ()
{
    HomeSortViewController *homeSortVC;

}
@end

@implementation HomeViewController


@synthesize searchBar, messageNumberLabel, homeScrollView, familiarButton, commerceButton, itemButton, serviceButton, enterpriseButton, overScrollView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
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
    [searchBar setBackgroundImage:[[UIImage alloc] init]];
    
    // Customize message number label
    messageNumberLabel.layer.cornerRadius = messageNumberLabel.frame.size.width / 2;
    messageNumberLabel.layer.masksToBounds = YES;

}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // InitializePageView
    [self initPageView];
}

#pragma mark - Initialize Page
- (void)initPageView {
    [self setNoSelectedAllButtons];
    familiarButton.selected = YES;
    
    HomeFamiliarViewController *homeFamiliarVC = [[HomeFamiliarViewController alloc] initWithNibName:@"HomeFamiliarViewController" bundle:nil];
    [homeFamiliarVC.view setFrame:CGRectMake(0, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeFamiliarVC.view];
    
    HomeEnterpriseViewController *homeEnterpriseVC = [[HomeEnterpriseViewController alloc] initWithNibName:@"HomeEnterpriseViewController" bundle:nil];
    [homeEnterpriseVC.view setFrame:CGRectMake(homeScrollView.frame.size.width, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeEnterpriseVC.view];
    
    HomeCommerceViewController *homeCommerceVC = [[HomeCommerceViewController alloc] initWithNibName:@"HomeCommerceViewController" bundle:nil];
    [homeCommerceVC.view setFrame:CGRectMake(homeScrollView.frame.size.width * 2, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeCommerceVC.view];
    
    HomeItemViewController *homeItemVC = [[HomeItemViewController alloc] initWithNibName:@"HomeItemViewController" bundle:nil];
    [homeItemVC.view setFrame:CGRectMake(homeScrollView.frame.size.width * 3, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeItemVC.view];
    
    HomeServiceViewController *homeServiceVC = [[HomeServiceViewController alloc] initWithNibName:@"HomeServiceViewController" bundle:nil];
    [homeServiceVC.view setFrame:CGRectMake(homeScrollView.frame.size.width * 4, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    [homeScrollView addSubview:homeServiceVC.view];
    
    homeSortVC = [[HomeSortViewController alloc] initWithNibName:@"HomeSortViewController" bundle:nil];
    [homeSortVC.view setFrame:CGRectMake(homeScrollView.frame.size.width * 1, 0, homeScrollView.frame.size.width, homeScrollView.frame.size.height)];
    
    [homeScrollView setContentSize:CGSizeMake(homeScrollView.frame.size.width * 5, homeScrollView.frame.size.height)];
}

#pragma mark - UIScrollViewDelegate
- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    static NSInteger previousPage = 0;
    CGFloat pageWidth = scrollView.frame.size.width;
    float fractionalPage = scrollView.contentOffset.x / pageWidth;
    NSInteger page = lround(fractionalPage);
    if (previousPage != page) {
        [self setNoSelectedAllButtons];
        previousPage = page;
        /* Page did change */
        switch (page) {
            case 0:
                familiarButton.selected = YES;
                break;
            case 1:
                enterpriseButton.selected = YES;
                break;
            case 2:
                commerceButton.selected = YES;
                break;
            case 3:
                itemButton.selected = YES;
                break;
            case 4:
                serviceButton.selected = YES;
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
        [self moveToPage:0];
        familiarButton.selected = YES;
    }
}

- (IBAction)enterpriseButtonAction:(id)sender {
    if(!enterpriseButton.selected)
    {
        [self moveToPage:1];
        enterpriseButton.selected = YES;
    }
}

- (IBAction)commerceButtonAction:(id)sender {
    if(!commerceButton.selected)
    {
        [self moveToPage:2];
        commerceButton.selected = YES;
    }
}

- (IBAction)itemButtonAction:(id)sender {
    if(!itemButton.selected)
    {
        [self moveToPage:3];
        itemButton.selected = YES;
    }
}

- (IBAction)serviceButtonAction:(id)sender {
    if(!serviceButton.selected)
    {
        [self moveToPage:4];
        serviceButton.selected = YES;
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
        [overScrollView addSubview:homeSortVC.view];
        [[NSNotificationCenter defaultCenter] postNotificationName:ACTIVITY_TRANS_TAB_NOTIFICATION object:nil];
        [UIView animateWithDuration:0.5f animations:^{
            [homeSortVC.view setFrame:CGRectMake(0, 0, homeSortVC.view.frame.size.width, homeSortVC.view.frame.size.height)];
        } completion:^(BOOL finished) {
            completedSortViewAnimation = YES;
        }];
    }else{
        sortButton.selected = NO;
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

- (void)moveToPage:(NSInteger)page {
    [self setNoSelectedAllButtons];
    CGRect frame = homeScrollView.frame;
    frame.origin.x = frame.size.width * page;
    frame.origin.y = 0;
    [homeScrollView scrollRectToVisible:frame animated:YES];
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
