//
//  HomeChoiceViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomePersonalChoiceViewController.h"
#import "HomeChoiceAllCityViewController.h"
#import "HomeChoiceBusinessViewController.h"

#import "Global.h"

@interface HomePersonalChoiceViewController ()
{
    NSString *choiceKind;
    NSString *choiceCity;
}
@end

@implementation HomePersonalChoiceViewController
@synthesize choiceCityButtons, choiceCategoryButtons;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    choiceCity = @"";
    choiceKind = @"";

    NSString *strCity = [CommonData sharedInstance].choiceFamiliarCity;
    
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

    NSString *strAkind = [CommonData sharedInstance].choiceFamiliarKind;
    
    if (strAkind.length == 0)
        [self setCategoryButtonSelected:0];
    else
        [self setCategoryButtonSelected:[strAkind integerValue]];

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
    [self makeRoundAllButtons];
}

- (void)makeRoundAllButtons {
    for(int i = 0; i < choiceCityButtons.count; i++) {
        ((UIButton *)choiceCityButtons[i]).layer.cornerRadius = ROUNDBUTTONRADIUS;
    }
    for(int i = 0; i < choiceCategoryButtons.count; i++) {
        ((UIButton *)choiceCategoryButtons[i]).layer.cornerRadius = ROUNDBUTTONRADIUS;
    }
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

- (void)setCategoryButtonSelected:(NSInteger)selectedIndex {
    [self AllCategoryButtonsDeselected];
    
    if (selectedIndex == 0)
        choiceKind = @"";
    else if (selectedIndex == 1)
        choiceKind = @"1";
    else if (selectedIndex == 2)
        choiceKind = @"2";
    
    [choiceCategoryButtons[selectedIndex] setTitleColor:SELECTED_BUTTON_TITLE_COLOR forState:UIControlStateNormal];
    [choiceCategoryButtons[selectedIndex] setBackgroundColor:SELECTED_BUTTON_BACKGROUND_COLOR];
}

- (void)AllCategoryButtonsDeselected {
    for(int i = 0; i < choiceCategoryButtons.count; i++) {
        [choiceCategoryButtons[i] setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [choiceCategoryButtons[i] setBackgroundColor:BLACK_COLOR_245];
    }
}

#pragma mark - IBAction
- (IBAction)choiceCityAction:(id)sender {
    UIButton *cityButton = (UIButton *)sender;
    [self setCityButtonSelected:cityButton.tag];
    if(cityButton.tag == (choiceCityButtons.count - 1)) {
        HomeChoiceAllCityViewController *homeChoiceAllCityVC = [[HomeChoiceAllCityViewController alloc] initWithNibName:@"HomeChoiceAllCityViewController" bundle:nil];
        [homeChoiceAllCityVC.view setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
        homeChoiceAllCityVC.homeIndex = SUB_HOME_PERSONAL;
        [self.navigationController pushViewController:homeChoiceAllCityVC animated:YES];
    }
}

- (IBAction)choiceCategoryAction:(id)sender {
    UIButton *categoryButton = (UIButton *)sender;
    [self setCategoryButtonSelected:categoryButton.tag];
}

- (IBAction)businessAction:(id)sender {
    HomeChoiceBusinessViewController *homeChoiceBusinessVC = [[HomeChoiceBusinessViewController alloc] initWithNibName:@"HomeChoiceBusinessViewController" bundle:nil];
    homeChoiceBusinessVC.isCancelButton = NO;
    homeChoiceBusinessVC.mChoice = CHOICE_HOME_PERSONAL;
    homeChoiceBusinessVC.isSingleSelectionMode = NO;
    
    [homeChoiceBusinessVC.view setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    [self.navigationController pushViewController:homeChoiceBusinessVC animated:NO];
}

- (IBAction)onResetAction:(id)sender {
    [self setCityButtonSelected:0];
    [self setCategoryButtonSelected:0];
    [CommonData sharedInstance].choiceFamiliarKind = @"";
    [CommonData sharedInstance].choiceFamiliarIds = @"";
    [CommonData sharedInstance].choiceFamiliarCity = @"";
}

- (IBAction)onChoiceAction:(id)sender {
    [CommonData sharedInstance].choiceFamiliarKind = choiceKind;
    [CommonData sharedInstance].choiceFamiliarCity = choiceCity;
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] postNotificationName:SET_HOMECHOICE_VIEW_NOTIFICATION object:nil];
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
