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

@end

@implementation HomeCommerceChoiceViewController
@synthesize choiceCityButtons;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSString *strCity = [CommonData sharedInstance].choiceCity;
    
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
    // Do any additional setup after loading the view.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onChoiceAction:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] postNotificationName:SET_HOMECHOICE_VIEW_NOTIFICATION object:nil];
}

- (IBAction)onResetAction:(id)sender {
    [CommonData sharedInstance].choicePleixingIds = @"";
    [self setCityButtonSelected:0];
}

- (void)setCityButtonSelected:(NSInteger)selectedIndex {
    [self AllCityButtonsDeselected];
    
    UIButton *button = choiceCityButtons[selectedIndex];
    
    if (selectedIndex == 0)
        [CommonData sharedInstance].choiceCity = @"";
    else if (selectedIndex == choiceCityButtons.count - 1 )
    {
        
    }
    else
        [CommonData sharedInstance].choiceCity = button.titleLabel.text;
    
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
        [self.navigationController pushViewController:homeChoiceAllCityVC animated:YES];
    }
}


- (IBAction)businessAction:(id)sender {
    HomeChoiceBusinessViewController *homeChoiceBusinessVC = [[HomeChoiceBusinessViewController alloc] initWithNibName:@"HomeChoiceBusinessViewController" bundle:nil];
    homeChoiceBusinessVC.mChoice = CHOICE_HOME_COMMERCE;
    homeChoiceBusinessVC.isSingleSelectionMode = NO;
    [homeChoiceBusinessVC.view setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    [self.navigationController pushViewController:homeChoiceBusinessVC animated:NO];
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
