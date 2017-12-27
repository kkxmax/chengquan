//
//  ChoiceCategoryViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/12/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ChoiceCategoryViewController.h"
#import "Global.h"

@interface ChoiceCategoryViewController ()
{
    NSMutableArray *categoryArray;
    NSMutableArray *cateButtonArray;
}
@end

@implementation ChoiceCategoryViewController
@synthesize delegate;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    categoryArray = [NSMutableArray array];
    cateButtonArray = [NSMutableArray array];
}

- (void)getData {
    switch (self.categoryType) {
        case PRODUCT_CATEGORY:
            [self getDataFromeServer:@"getPleixingList"];
            break;
        case ITEM_CATEGORY:
            [self getDataFromeServer:@"getItemFenleiList"];
            break;
        case SERVICE_CATEGORY:
            [self getDataFromeServer:@"getServiceFenleiList"];
            break;
        default:
            break;
    }
}

- (void)getDataFromeServer:(NSString *)actionName {
    [categoryArray removeAllObjects];
    [cateButtonArray removeAllObjects];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:actionName forKey:@"pAct"];
    if(self.categoryType == PRODUCT_CATEGORY) {
        [dicParams setObject:self.pleixingID forKey:@"pleixingId"];
    }
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:actionName Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                categoryArray = (NSMutableArray *)(dicRes[@"data"]);
                [self setData];
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

- (void)setData {
    CGRect frame = self.view.frame;
    float width = frame.size.width / 3.f;

    for (int i = 0; i < categoryArray.count; i++) {
        
        NSDictionary *categoryDic = categoryArray[i];
        int x = (i % 3) * width;
        int y = (i / 3) * 55;
        
        UIButton *button = [[UIButton alloc] initWithFrame:CGRectMake(x + 10, y + 49, width - 24, 36)];
        button.tag = [categoryDic[@"id"] integerValue];
        [button setBackgroundColor:BLACK_COLOR_245];
        [button setTitle:categoryDic[@"title"] forState:UIControlStateNormal];
        [button setTitleColor:BLACK_COLOR_51 forState:UIControlStateNormal];
        [button.titleLabel setFont:FONT_12];
        [button addTarget:self action:@selector(onClickCategoryButton:) forControlEvents:UIControlEventTouchUpInside];
        
        NSString *strId = [NSString stringWithFormat:@"%d", (int)button.tag];
        if (self.selectedCategory && (button.tag == [self.selectedCategory[@"id"] integerValue]))
        {
            [button setTitleColor:SELECTED_BUTTON_TITLE_COLOR forState:UIControlStateNormal];
            [button setBackgroundColor:SELECTED_BUTTON_BACKGROUND_COLOR];
            [cateButtonArray addObject:strId];
        }
        [self.view addSubview:button];
    }
}

- (void) onClickCategoryButton:(UIButton*) button {
    
    NSString *strId = [NSString stringWithFormat:@"%d", (int)button.tag];
    if ([cateButtonArray containsObject:strId]) {
        [button setTitleColor:BLACK_COLOR_51 forState:UIControlStateNormal];
        [button setBackgroundColor:BLACK_COLOR_245];
        [cateButtonArray removeObject:strId];
    }else{
        [self allDisableButton];
        [button setTitleColor:SELECTED_BUTTON_TITLE_COLOR forState:UIControlStateNormal];
        [button setBackgroundColor:SELECTED_BUTTON_BACKGROUND_COLOR];
        [cateButtonArray addObject:strId];
        for(int i = 0; i < categoryArray.count; i++) {
            NSDictionary *tmpDic = (NSDictionary *)[categoryArray objectAtIndex:i];
            if(button.tag == [tmpDic[@"id"] integerValue]) {
                self.selectedCategory = (NSDictionary *)[categoryArray objectAtIndex:i];
            }
        }
    }
    
}

- (void)allDisableButton {
    for(int i = 0; i < categoryArray.count; i++) {
        NSDictionary *tmpDic = (NSDictionary *)[categoryArray objectAtIndex:i];
        NSInteger tagIndex =  [tmpDic[@"id"] integerValue];
        UIButton *button = (UIButton *)[self.view viewWithTag:tagIndex];
        [button setTitleColor:BLACK_COLOR_51 forState:UIControlStateNormal];
        [button setBackgroundColor:BLACK_COLOR_245];
    }
    [cateButtonArray removeAllObjects];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - IBAction
- (IBAction)onResetAction:(id)sender {
    [delegate hideChoiceCategoryView:nil];
}

- (IBAction)onChoiceAction:(id)sender {
    [delegate hideChoiceCategoryView:self.selectedCategory];
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
