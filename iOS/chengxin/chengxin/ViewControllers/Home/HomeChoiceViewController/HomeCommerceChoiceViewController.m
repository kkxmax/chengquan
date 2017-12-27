//
//  HomeCommerceChoiceViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/9/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeCommerceChoiceViewController.h"
#import "HomeChoiceAllCityViewController.h"
#import "HomeChoiceBusinessViewController.h"
#import "Global.h"
@interface HomeCommerceChoiceViewController ()
{
    NSString *choiceCity;
    HomeChoiceBusinessViewController *homeChoiceBusinessVC;
}
@end

@implementation HomeCommerceChoiceViewController
@synthesize choiceCityButtons;

- (void)viewDidLoad {
    [super viewDidLoad];
    choiceCity = @"";
    NSString *strCity = [CommonData sharedInstance].choiceCommerceCity;
    
    if (strCity.length == 0)
        [self setCityButtonSelected:0];
    else
    {
        for(int i = 0; i < choiceCityButtons.count; i++) {
            UIButton *button = choiceCityButtons[i];
            if ([strCity isEqualToString:button.titleLabel.text])
            {
                [self setCityButtonSelected:i];
                break;
            }
        }
    }
    
    homeChoiceBusinessVC = [[HomeChoiceBusinessViewController alloc] initWithNibName:@"HomeChoiceBusinessViewController" bundle:nil];
    homeChoiceBusinessVC.isCancelButton = NO;
    homeChoiceBusinessVC.mChoice = CHOICE_HOME_COMMERCE;
    homeChoiceBusinessVC.isSingleSelectionMode = NO;
    [self.commerceCategoryView addSubview:homeChoiceBusinessVC.view];

    // Do any additional setup after loading the view.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
    homeChoiceBusinessVC.titleViewHeight.constant = 0;
    homeChoiceBusinessVC.buttonViewHeight.constant = 0;
    homeChoiceBusinessVC.buttonView.hidden = YES;
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [homeChoiceBusinessVC.view setFrame:CGRectMake(0, 0, self.commerceCategoryView.frame.size.width, self.commerceCategoryView.frame.size.height)];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onChoiceAction:(id)sender {
    [CommonData sharedInstance].choiceCommerceCity = choiceCity;
    [homeChoiceBusinessVC selectAction:nil];
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] postNotificationName:SET_HOMECHOICE_VIEW_NOTIFICATION object:nil];
}

- (IBAction)onResetAction:(id)sender {
    [CommonData sharedInstance].choiceCommerceCity = @"";
    [CommonData sharedInstance].choiceCommerceKind = @"";
    [CommonData sharedInstance].choiceCommerceIds = @"";
    [self setCityButtonSelected:0];
    [homeChoiceBusinessVC onClickCancelBtn:nil];
}

- (void)setCityButtonSelected:(NSInteger)selectedIndex {
    [self AllCityButtonsDeselected];
    
    UIButton *button = choiceCityButtons[selectedIndex];
    
    if (selectedIndex == 0)
        choiceCity = @"";
    else if (selectedIndex == choiceCityButtons.count - 1 )
    {
        
    }
    else
        choiceCity = button.titleLabel.text;
    
    [choiceCityButtons[selectedIndex] setTitleColor:SELECTED_BUTTON_TITLE_COLOR forState:UIControlStateNormal];
    [choiceCityButtons[selectedIndex] setBackgroundColor:SELECTED_BUTTON_BACKGROUND_COLOR];
}

- (void)AllCityButtonsDeselected {
    for(int i = 0; i < choiceCityButtons.count; i++) {
        [choiceCityButtons[i] setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [choiceCityButtons[i] setBackgroundColor:BLACK_COLOR_245];
    }
}

#pragma mark - IBAction
- (IBAction)choiceCityAction:(id)sender {
    UIButton *cityButton = (UIButton *)sender;
    [self setCityButtonSelected:cityButton.tag];
    if(cityButton.tag == (choiceCityButtons.count - 1)) {
        HomeChoiceAllCityViewController *homeChoiceAllCityVC = [[HomeChoiceAllCityViewController alloc] initWithNibName:@"HomeChoiceAllCityViewController" bundle:nil];
        [homeChoiceAllCityVC.view setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
        homeChoiceAllCityVC.homeIndex = SUB_HOME_COMMERCE;
        [self.navigationController pushViewController:homeChoiceAllCityVC animated:YES];
    }
}


- (IBAction)businessAction:(id)sender {
//    HomeChoiceBusinessViewController *homeChoiceBusinessVC = [[HomeChoiceBusinessViewController alloc] initWithNibName:@"HomeChoiceBusinessViewController" bundle:nil];
//    homeChoiceBusinessVC.isCancelButton = NO;
//    homeChoiceBusinessVC.mChoice = CHOICE_HOME_COMMERCE;
//    homeChoiceBusinessVC.isSingleSelectionMode = NO;
//    [homeChoiceBusinessVC.view setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
//    [self.navigationController pushViewController:homeChoiceBusinessVC animated:NO];
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
