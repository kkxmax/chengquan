//
//  HomeItemChoiceViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/9/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeItemChoiceViewController.h"
#import "HomeChoiceAllCityViewController.h"
#import "Global.h"

@interface HomeItemChoiceViewController ()
{
    NSMutableArray *aryItemList;
    NSMutableArray *aryFenleiChoice;
    NSString *choiceKind;
    NSString *choiceCity;
}
@end

@implementation HomeItemChoiceViewController
@synthesize choiceCityButtons, choiceCategoryButtons;
@synthesize viewContent;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    choiceCity = @"";
    choiceKind = @"";

    aryItemList = [[NSMutableArray alloc] init];
    
    NSString *strChoice = @"";
    strChoice = [CommonData sharedInstance].choiceItemIds;
    if (strChoice.length == 0)
        aryFenleiChoice = [[NSMutableArray alloc] init];
    else
        aryFenleiChoice = [[NSMutableArray alloc] initWithArray:[strChoice componentsSeparatedByString:@","]];

    
    NSString *strCity = [CommonData sharedInstance].choiceItemCity;

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

    NSString *strAkind = [CommonData sharedInstance].choiceItemKind;
    
    if (strAkind.length == 0)
        [self setCategoryButtonSelected:0];
    else
        [self setCategoryButtonSelected:[strAkind integerValue]];
    
    [self getItemCategoryListFromServer];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self.itemChoiceScrollView setContentSize:CGSizeMake(self.itemChoiceScrollView.frame.size.width, 700)];
    [self makeRoundAllButtons];
}

- (void) getItemCategoryListFromServer {
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"getItemFenleiList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETFENLEILIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                
                [aryItemList removeAllObjects];
                
                for (int i = 0; i < aryData.count; i++) {
                    [aryItemList addObject:aryData[i]];
                }
                
                [self createItemListView];
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
    }];
}

- (void)createItemListView {
    for (UIView *view in viewContent.subviews)
        if ([view isKindOfClass:[UIButton class]])
            [view removeFromSuperview];
    
    int count = (int)aryItemList.count;

    CGRect frame = viewContent.frame;
    frame.size.height = 44.f * ceil(count / 3.f) + 60;
    [viewContent setFrame:frame];
    
    float width = (frame.size.width - 10) / 3.f;
    
    for (int i = 0; i < count; i++) {
        
        NSDictionary *dicChild = aryItemList[i];
        int x = (i % 3) * width;
        int y = (i / 3) * 44 + 49;
        
        UIButton *button = [[UIButton alloc] initWithFrame:CGRectMake(x + 10, y, width - 10, 38)];
        button.tag = [dicChild[@"id"] integerValue];
        [button setBackgroundColor:BLACK_COLOR_245];
        [button setTitle:dicChild[@"title"] forState:UIControlStateNormal];
        [button setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [button.titleLabel setFont:FONT_12];
        button.layer.cornerRadius = ROUNDBUTTONRADIUS;

        [button addTarget:self action:@selector(onClickItemButton:) forControlEvents:UIControlEventTouchUpInside];
        
        NSString *strId = [NSString stringWithFormat:@"%d", (int)button.tag];
        if ([aryFenleiChoice containsObject:strId])
        {
            [button setTitleColor:SELECTED_BUTTON_TITLE_COLOR forState:UIControlStateNormal];
            [button setBackgroundColor:SELECTED_BUTTON_BACKGROUND_COLOR];
            [aryFenleiChoice addObject:strId];
        }
        [viewContent addSubview:button];
    }
}

- (void)onClickItemButton:(UIButton*) button {
    NSString *strId = [NSString stringWithFormat:@"%d", (int)button.tag];
    
    if([button.currentTitleColor isEqual:SELECTED_BUTTON_TITLE_COLOR])
    {
        [button setTitleColor:BLACK_COLOR_51 forState:UIControlStateNormal];
        [button setBackgroundColor:BLACK_COLOR_245];
        [aryFenleiChoice removeObject:strId];
    }
    else
    {
        [button setTitleColor:SELECTED_BUTTON_TITLE_COLOR forState:UIControlStateNormal];
        [button setBackgroundColor:SELECTED_BUTTON_BACKGROUND_COLOR];
        [aryFenleiChoice addObject:strId];
    }

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

- (IBAction)choiceCityAction:(id)sender {
    UIButton *cityButton = (UIButton *)sender;
    [self setCityButtonSelected:cityButton.tag];
    if(cityButton.tag == (choiceCityButtons.count - 1)) {
        HomeChoiceAllCityViewController *homeChoiceAllCityVC = [[HomeChoiceAllCityViewController alloc] initWithNibName:@"HomeChoiceAllCityViewController" bundle:nil];
        [homeChoiceAllCityVC.view setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
        homeChoiceAllCityVC.homeIndex = SUB_HOME_ITEM;
        [self.navigationController pushViewController:homeChoiceAllCityVC animated:YES];
    }
}

- (IBAction)choiceCategoryAction:(id)sender {
    UIButton *categoryButton = (UIButton *)sender;
    [self setCategoryButtonSelected:categoryButton.tag];
}

- (IBAction)onChoiceAction:(id)sender {
    
    if (aryFenleiChoice.count > 0) {
        NSString *strChoice = @"";
        for (int i = 0; i < aryFenleiChoice.count; i++) {
            NSString *choice = aryFenleiChoice[i];
            if (i == 0)
                strChoice = choice;
            else
                strChoice = [NSString stringWithFormat:@"%@,%@", strChoice, choice];
        }
        [CommonData sharedInstance].choiceItemIds = strChoice;
    }
    else {

        [CommonData sharedInstance].choiceItemIds = @"";
    }
    [CommonData sharedInstance].choiceItemKind = choiceKind;
    [CommonData sharedInstance].choiceItemCity = choiceCity;
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_HOMECHOICE_VIEW_NOTIFICATION object:nil];
    [[NSNotificationCenter defaultCenter] postNotificationName:SET_HOMECHOICE_VIEW_NOTIFICATION object:nil];

}

- (IBAction)onResetAction:(id)sender {
    [self setCityButtonSelected:0];
    [self setCategoryButtonSelected:0];
    [CommonData sharedInstance].choiceItemKind = @"";
    [CommonData sharedInstance].choiceItemIds = @"";
    [CommonData sharedInstance].choiceItemCity = @"";
    [aryFenleiChoice removeAllObjects];
    [self createItemListView];
}


@end
