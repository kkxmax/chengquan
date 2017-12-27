//
//  HomeChoiceBusinessViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeChoiceBusinessViewController.h"
#import "HomeChoiceBusinessTableViewCell.h"
#import "Global.h"

@interface HomeChoiceBusinessViewController ()
{
    NSMutableArray *aryCategory;
    NSMutableArray *aryCategorySelect;
    HomeChoiceBusinessTableViewCell *selectCell;
    NSMutableArray *aryEvalChoice;
    NSDictionary *selectCommerceDic;
}
@end

@implementation HomeChoiceBusinessViewController
@synthesize businessTableView, mChoice, delegate;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [businessTableView registerNib:[UINib nibWithNibName:@"HomeChoiceBusinessTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeChoiceBusinessCellIdentifier"];
    
    aryCategory = [[NSMutableArray alloc] init];
    aryCategorySelect = [[NSMutableArray alloc] init];
    selectCommerceDic = nil;
    
    NSString *strChoice = @"";
    if (mChoice == CHOICE_HOME_COMMERCE || mChoice == CHOICE_ADD_COMMERCE)
        strChoice = [CommonData sharedInstance].choiceCommerceIds;
    else {
        strChoice = [CommonData sharedInstance].choiceEvaluateIds;
        self.navTitleLabel.text = @"行业";
    }

    if (mChoice == CHOICE_HOME_PERSONAL ) {
        strChoice = [CommonData sharedInstance].choiceFamiliarIds;
    }
    if (mChoice == CHOICE_HOME_ENTERPRISE ) {
        strChoice = [CommonData sharedInstance].choiceEnterpriseIds;
    }

    if (strChoice.length == 0)
        aryEvalChoice = [[NSMutableArray alloc] init];
    else
        aryEvalChoice = [[NSMutableArray alloc] initWithArray:[strChoice componentsSeparatedByString:@","]];
    
    if (mChoice == CHOICE_HOME_COMMERCE || mChoice == CHOICE_ADD_COMMERCE) {
        [self getCommerceListFromServer];
    }
    else
        [self getBusinessListFromServer];
    
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];

    [self.cancelButton setTitle:self.isCancelButton ? @"取消" : @"重置" forState:UIControlStateNormal];
    self.navigationController.navigationBarHidden = YES;
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    if(mChoice == CHOICE_ADD_COMMERCE) {
        self.limitView.hidden = YES;
        self.limitViewHeight.constant = 0.f;
    }else{
        self.limitViewHeight.constant = 45.f;
        self.limitView.hidden = NO;
    }
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) getBusinessListFromServer {
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"getXyleixingList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETXYLEIXINGLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                
                [aryCategory removeAllObjects];
                [aryCategorySelect removeAllObjects];
                
                for (int i = 0; i < aryData.count; i++) {
                    [aryCategory addObject:aryData[i]];
                    [aryCategorySelect addObject:@"0"];
                }
                
                if ( aryCategory.count > 0 )
                {
                    [businessTableView reloadData];
                    
                }
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

- (void) getCommerceListFromServer {
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"getPleixingList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETXYLEIXINGLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                
                [aryCategory removeAllObjects];
                [aryCategorySelect removeAllObjects];
                
                for (int i = 0; i < aryData.count; i++) {
                    [aryCategory addObject:aryData[i]];
                    [aryCategorySelect addObject:@"0"];
                }
                
                if ( aryCategory.count > 0 )
                {
                    [businessTableView reloadData];
                    
                }
            }
        }
    }];
}
#pragma mark - IBAction
- (IBAction)cancelAction:(id)sender {
    if (mChoice == CHOICE_HOME_PERSONAL || mChoice == CHOICE_HOME_COMMERCE || mChoice == CHOICE_HOME_ENTERPRISE)
        [self.navigationController popViewControllerAnimated:YES];
    else if (mChoice == CHOICE_EVAL)
    {
        for (int i = 0; i < aryCategorySelect.count; i++) {
            [aryCategorySelect replaceObjectAtIndex:i withObject:@"0"];
        }
        [aryEvalChoice removeAllObjects];
        [businessTableView reloadData];
        [[NSNotificationCenter defaultCenter] postNotificationName:SET_EVALCHOICE_VIEW_NOTIFICATION object:nil];
    }else if (mChoice == CHOICE_ADD_COMMERCE) {
        [delegate hideChoiceBussinessView:nil];
        return;
    }
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_REALNAME_CHOICE_VIEW_NOTIFICATION object:nil];
}

- (IBAction)selectAction:(id)sender {
    if (mChoice == CHOICE_ADD_COMMERCE) {
        [delegate hideChoiceBussinessView:selectCommerceDic];
        return;
    }
    NSString *strChoice = @"";
    if (aryEvalChoice.count > 0) {
        for (int i = 0; i < aryEvalChoice.count; i++) {
            NSString *choice = aryEvalChoice[i];
            if (i == 0)
                strChoice = choice;
            else
                strChoice = [NSString stringWithFormat:@"%@,%@", strChoice, choice];
        }
    }
    if(self.isSingleSelectionMode)
    {
        for(NSDictionary* item in aryCategory)
        {
            for(NSDictionary* subItem in item[@"children"])
            {
                if([subItem[@"id"] intValue] == [strChoice intValue])
                {
                    [CommonData sharedInstance].selectedBusiness = subItem[@"title"];
                    break;
                }
            }
        }
        [CommonData sharedInstance].choiceXyleixingId = strChoice;
    }
    
    if (mChoice == CHOICE_HOME_PERSONAL ) {
        [CommonData sharedInstance].choiceFamiliarIds = strChoice;
    }
    if (mChoice == CHOICE_HOME_ENTERPRISE ) {
        [CommonData sharedInstance].choiceEnterpriseIds = strChoice;
    }
    if( mChoice == CHOICE_EVAL) {
        [CommonData sharedInstance].choiceEvaluateIds = strChoice;
    }
    else if (mChoice == CHOICE_HOME_COMMERCE)
        [CommonData sharedInstance].choiceCommerceIds = strChoice;

    if (mChoice == CHOICE_HOME_PERSONAL || mChoice == CHOICE_HOME_ENTERPRISE || mChoice == CHOICE_HOME_COMMERCE)
        [self.navigationController popViewControllerAnimated:YES];
    else if (mChoice == CHOICE_EVAL) {
        [[NSNotificationCenter defaultCenter] postNotificationName:SET_EVALCHOICE_VIEW_NOTIFICATION object:nil];
    }
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_REALNAME_CHOICE_VIEW_NOTIFICATION object:nil];
}

-(void) onClickContentButton:(UIButton*) button {
    int index = (int)button.tag;
    NSIndexPath *path = [NSIndexPath indexPathForRow:index inSection:0];
    HomeChoiceBusinessTableViewCell *cell = [businessTableView cellForRowAtIndexPath:path];
    if(cell.extendButton.isSelected) {
        cell.extendButton.selected = NO;
        cell.viewContent.hidden = YES;
        [aryCategorySelect replaceObjectAtIndex:index withObject:@"0"];
    }else{
        cell.extendButton.selected = YES;
        cell.viewContent.hidden = NO;
        [aryCategorySelect replaceObjectAtIndex:index withObject:@"1"];
    }
    //[businessTableView reloadData];
    
    [businessTableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:path, nil] withRowAnimation:UITableViewRowAnimationNone];
}

#pragma mark - UITableViewDelegate, UITableViewDataSource

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 45.f;

    NSString *show = aryCategorySelect[indexPath.row];
    if ([show integerValue] == 1) {
        NSDictionary *dic = aryCategory[indexPath.row];
        if (dic == nil)
            return homeTableCellHeight;
        
        NSArray *childBussiness = dic[@"children"];
        
        homeTableCellHeight += 55.f * ceil(childBussiness.count / 3.f);
    }

    
    return homeTableCellHeight;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HomeChoiceBusinessTableViewCell *cell = (HomeChoiceBusinessTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeChoiceBusinessCellIdentifier" forIndexPath:indexPath];

    NSDictionary *dic = aryCategory[indexPath.row];
    cell.lblName.text = dic[@"title"];
    cell.btnContent.tag = indexPath.row;
    [cell.btnContent addTarget:self action:@selector(onClickContentButton:) forControlEvents:UIControlEventTouchUpInside];
    
    if ([aryCategorySelect[indexPath.row] integerValue] == 1)
    {
        NSArray *aryChild = dic[@"children"];
        CGRect frame = cell.viewContent.frame;
        frame.size.height = 55.f * ceil(aryChild.count / 3.f);
        int count = (int)aryChild.count;
        float width = frame.size.width / 3.f;
        
        for (UIView *view in cell.viewContent.subviews)
             [view removeFromSuperview];
        
        for (int i = 0; i < count; i++) {
            
            NSDictionary *dicChild = aryChild[i];
            int x = (i % 3) * width;
            int y = (i / 3) * 55;
            
            UIButton *button = [[UIButton alloc] initWithFrame:CGRectMake(x + 12, y + 10, width - 24, 36)];
            button.tag = [dicChild[@"id"] integerValue];
            [button setBackgroundColor:WHITE_COLOR];
            [button setTitle:dicChild[@"title"] forState:UIControlStateNormal];
            [button setTitleColor:BLACK_COLOR_51 forState:UIControlStateNormal];
            [button.titleLabel setFont:FONT_12];
            [button addTarget:self action:@selector(onClickBusinessButton:) forControlEvents:UIControlEventTouchUpInside];
            
            NSString *strId = [NSString stringWithFormat:@"%d", (int)button.tag];
            if ([aryEvalChoice containsObject:strId])
            {
                [button setTitleColor:SELECTED_BUTTON_TITLE_COLOR forState:UIControlStateNormal];
                [button setBackgroundColor:SELECTED_BUTTON_BACKGROUND_COLOR];
                self.allSelectLabel.textColor = BLACK_COLOR_51;
                if(mChoice == CHOICE_ADD_COMMERCE) {
                    selectCommerceDic = dicChild;
                }

            }
            [cell.viewContent addSubview:button];
        }
        
        cell.viewContent.hidden = NO;
        cell.extendButton.selected = YES;
    }
    else
    {
        cell.viewContent.hidden = YES;
        cell.extendButton.selected = NO;
    }
    
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return aryCategory.count;
}

- (void) onClickBusinessButton:(UIButton*) button {
    
    NSString *strId = [NSString stringWithFormat:@"%d", (int)button.tag];
    if (mChoice == CHOICE_ADD_COMMERCE) {
        [aryEvalChoice removeAllObjects];
    }
    if([button.currentTitleColor isEqual:SELECTED_BUTTON_TITLE_COLOR])
    {
        [button setTitleColor:BLACK_COLOR_51 forState:UIControlStateNormal];
        [button setBackgroundColor:WHITE_COLOR];
        if (self.isSingleSelectionMode)
            [aryEvalChoice removeAllObjects];
        else
            [aryEvalChoice removeObject:strId];
    }
    else
    {
        [button setTitleColor:SELECTED_BUTTON_TITLE_COLOR forState:UIControlStateNormal];
        [button setBackgroundColor:SELECTED_BUTTON_BACKGROUND_COLOR];
        
        if (self.isSingleSelectionMode) {
            [aryEvalChoice removeAllObjects];
            [aryEvalChoice addObject:strId];
            [CommonData sharedInstance].selectedBusiness = button.titleLabel.text;
        }
        else
            [aryEvalChoice addObject:strId];
    }
    
    if (self.isSingleSelectionMode || mChoice == CHOICE_ADD_COMMERCE)
        [businessTableView reloadData];
}

- (IBAction)onClickAllAction:(id)sender {
    [aryEvalChoice removeAllObjects];
    [businessTableView reloadData];
    [aryEvalChoice addObject:@""];
    selectCommerceDic = nil;
    self.allSelectLabel.textColor = SELECTED_BUTTON_TITLE_COLOR;

}
-(IBAction)onClickCancelBtn:(id)sender
{
    if(self.isCancelButton)
    {
        [self cancelAction:nil];
    }else
    {
        [self onClickAllAction:nil];
        [self selectAction:nil];
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
