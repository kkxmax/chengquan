//
//  HomeChoiceViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeChoiceViewController.h"
#import "HomeChoiceAllCityViewController.h"
#import "HomeChoiceBusinessViewController.h"

#import "Global.h"

@interface HomeChoiceViewController ()

@end

@implementation HomeChoiceViewController
@synthesize choiceCityButtons, choiceCategoryButtons;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self setCityButtonSelected:0];
    [self setCategoryButtonSelected:0];
    
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
        [self.navigationController pushViewController:homeChoiceAllCityVC animated:YES];
    }
}

- (IBAction)choiceCategoryAction:(id)sender {
    UIButton *categoryButton = (UIButton *)sender;
    [self setCategoryButtonSelected:categoryButton.tag];
}

- (IBAction)businessAction:(id)sender {
    HomeChoiceBusinessViewController *homeChoiceBusinessVC = [[HomeChoiceBusinessViewController alloc] initWithNibName:@"HomeChoiceBusinessViewController" bundle:nil];
    [homeChoiceBusinessVC.view setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    [self.navigationController pushViewController:homeChoiceBusinessVC animated:NO];
}

- (IBAction)setChoiceAction:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_HOMECHOICE_VIEW_NOTIFICATION object:nil];
}

- (IBAction)setSetupAction:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_HOMECHOICE_VIEW_NOTIFICATION object:nil];
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
