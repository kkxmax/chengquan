//
//  EvalAwakenViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/4/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "EvalAwakenViewController.h"
#import "EvaluateAwakenTableViewCell.h"
#import "Global.h"
#import "UIImageView+WebCache.h"
#import "EvaluateDetailViewController.h"

@interface EvalAwakenViewController ()
{
    int tblHeight;
    NSMutableArray *frontEvaluateArray;
    NSMutableArray *backEvaluateArray;
}
@end

enum {
    SELECT_FRONT = 0,
    SELECT_BACK
};

@implementation EvalAwakenViewController
@synthesize scrollContentView, tblFrontView, tblBackView;
@synthesize btnFront, btnBack, sepFront, sepBack;
@synthesize selectType;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    tblHeight = SCREEN_HEIGHT - NAVIGATION_HEIGHT - 40;
    scrollContentView.delegate = self;
    [scrollContentView setContentSize:CGSizeMake(SCREEN_WIDTH * 2, tblHeight)];
    
    self.frontMarkLabel.layer.cornerRadius = self.frontMarkLabel.frame.size.width / 2;
    self.backMarkLabel.layer.cornerRadius = self.backMarkLabel.frame.size.width / 2;
    
    frontEvaluateArray = [NSMutableArray array];
    backEvaluateArray = [NSMutableArray array];
    
    [self onClickFrontButton:nil];
    
    if(self.isMineEvaluate) {
        [self getMyEvalFromServer];
    }else{
        [self getOtherEvalFromServer];
    }
}

- (void)getMyEvalFromServer {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getMyEstimateList" forKey:@"pAct"];
    [dicParams setObject:@"" forKey:@"start"];
    [dicParams setObject:@"" forKey:@"length"];
    [dicParams setObject:@"" forKey:@"akind"];
    [dicParams setObject:@"" forKey:@"kind"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETMYEVALUATELIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *evalList = (NSArray *)(dicRes[@"data"]);
                for(int i = 0; i < evalList.count; i++) {
                    NSDictionary *evalDic = (NSDictionary *)(evalList[i]);
                    if([evalDic[@"kind"] intValue] == 1) {
                        [frontEvaluateArray addObject:evalDic];
                    }else{
                        [backEvaluateArray addObject:evalDic];
                    }
                }
                if(frontEvaluateArray.count > 0) {
                    self.frontMarkLabel.hidden = NO;
                    self.frontMarkLabel.text = [NSString stringWithFormat:@"%d", (int)(frontEvaluateArray.count)];
                }else{
                    self.frontMarkLabel.hidden = YES;
                }
                if(backEvaluateArray.count > 0) {
                    self.backMarkLabel.hidden = NO;
                    self.backMarkLabel.text = [NSString stringWithFormat:@"%d", (int)(backEvaluateArray.count)];
                }else{
                    self.backMarkLabel.hidden = YES;
                }
                if(selectType == SELECT_FRONT) {
                    [tblFrontView reloadData];
                }else{
                    [tblBackView reloadData];
                }
            }
        }
    }];
}

- (void)getOtherEvalFromServer {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getEstimateToMeList" forKey:@"pAct"];
    [dicParams setObject:@"" forKey:@"start"];
    [dicParams setObject:@"" forKey:@"length"];
    [dicParams setObject:@"" forKey:@"akind"];
    [dicParams setObject:@"" forKey:@"kind"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETOTHEREVALUATELIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *evalList = (NSArray *)(dicRes[@"data"]);
                for(int i = 0; i < evalList.count; i++) {
                    NSDictionary *evalDic = (NSDictionary *)(evalList[i]);
                    if([evalDic[@"kind"] intValue] == 1) {
                        [frontEvaluateArray addObject:evalDic];
                    }else{
                        [backEvaluateArray addObject:evalDic];
                    }
                }
                if(frontEvaluateArray.count > 0) {
                    self.frontMarkLabel.hidden = NO;
                    self.frontMarkLabel.text = [NSString stringWithFormat:@"%d", (int)(frontEvaluateArray.count)];
                }else{
                    self.frontMarkLabel.hidden = YES;
                }
                if(backEvaluateArray.count > 0) {
                    self.backMarkLabel.hidden = NO;
                    self.backMarkLabel.text = [NSString stringWithFormat:@"%d", (int)(backEvaluateArray.count)];
                }else{
                    self.backMarkLabel.hidden = YES;
                }
                if(selectType == SELECT_FRONT) {
                    [tblFrontView reloadData];
                }else{
                    [tblBackView reloadData];
                }
            }
        }
    }];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void) selectItem:(NSInteger) type {
    [btnFront setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    [btnBack setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    
    sepFront.hidden = YES;
    sepBack.hidden = YES;
    
    if (type == SELECT_FRONT) {
        [btnFront setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
        sepFront.hidden = NO;
    }
    else if (type == SELECT_BACK) {
        [btnBack setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
        sepBack.hidden = NO;
    }
}

- (IBAction)onClickFrontButton:(id)sender {
    selectType = SELECT_FRONT;
    [self selectItem:selectType];
    [self createFrontView];
    [scrollContentView setContentOffset:CGPointMake(0, 0)];
}

- (IBAction)onClickBackButton:(id)sender {
    selectType = SELECT_BACK;
    [self selectItem:selectType];
    [self createBackView];
    [scrollContentView setContentOffset:CGPointMake(SCREEN_WIDTH, 0)];
}

- (void) createFrontView {
    if (tblFrontView == nil) {
        tblFrontView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, tblHeight)];
        [tblFrontView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
        tblFrontView.dataSource = self;
        tblFrontView.delegate = self;
        [tblFrontView registerNib:[UINib nibWithNibName:@"EvaluateAwakenTableViewCell" bundle:nil] forCellReuseIdentifier:@"EvaluateAwakeCellIdentifier"];

        [scrollContentView addSubview:tblFrontView];
    }else{
        [tblFrontView reloadData];
    }
}

- (void) createBackView {
    if (tblBackView == nil) {
        tblBackView = [[UITableView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH, 0, SCREEN_WIDTH, tblHeight)];
        [tblBackView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
        tblBackView.dataSource = self;
        tblBackView.delegate = self;
        [tblBackView registerNib:[UINib nibWithNibName:@"EvaluateAwakenTableViewCell" bundle:nil] forCellReuseIdentifier:@"EvaluateAwakeCellIdentifier"];
        [scrollContentView addSubview:tblBackView];
    }else{
        [tblBackView reloadData];
    }
}

#pragma mark - UIScrollViewDelegate

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    if ( scrollView == scrollContentView ) {
        CGFloat pageWidth = scrollView.frame.size.width;
        float fractionalPage = scrollView.contentOffset.x / pageWidth;
        NSInteger page = lround(fractionalPage);
        
        if (page == SELECT_FRONT) {
            selectType = SELECT_FRONT;
            [self createFrontView];
        }
        else if (page == SELECT_BACK)
        {
            selectType = SELECT_BACK;
            [self createBackView];
        }
        
        [self selectItem:selectType];
    }
}

#pragma mark -  UITableViewDelegate & UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if(selectType == SELECT_FRONT) {
        return frontEvaluateArray.count;
    }else{
        return backEvaluateArray.count;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 224.f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *simpleTableIdentifier = @"EvaluateAwakeCellIdentifier";
    EvaluateAwakenTableViewCell *cell = (EvaluateAwakenTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    NSDictionary *evalDic;
    if ( tableView == tblFrontView)
    {
        evalDic = (NSDictionary *)(frontEvaluateArray[indexPath.row]);
    }
    else if ( tableView == tblBackView )
    {
        evalDic = (NSDictionary *)(backEvaluateArray[indexPath.row]);
    }
    NSString *logoImageName = evalDic[@"ownerLogo"];
    if(![logoImageName isEqualToString:@""]) {
        [cell.avatarImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, logoImageName]]];
    }

    long ownerKind = [evalDic[@"ownerAkind"] longValue];
    if (ownerKind == PERSONAL_KIND) {
        cell.nameLabel.text = evalDic[@"ownerRealname"];
    }else{
        cell.nameLabel.text = evalDic[@"ownerEnterName"];
    }
    cell.evaluateLabel.text = evalDic[@"kindName"];
    cell.commentLabel.text = evalDic[@"content"];
    cell.timeLabel.text = evalDic[@"writeTimeString"];
    cell.agreeCountLabel.text = [NSString stringWithFormat:@"%ld",[evalDic[@"targetAccountElectCnt"] longValue]];
    cell.evaluateCountLabel.text = [NSString stringWithFormat:@"%ld",[evalDic[@"targetAccountFeedbackCnt"] longValue]];
    
    NSArray *imageArray = evalDic[@"imgPaths"];
    if(imageArray.count > 0) {
        for (int i = 0; i < imageArray.count; i++)
        {
            UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * 120, 0, 113, 80)];
            NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, imageArray[i]];
            [imgView sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
            [cell.evaluateScrollView addSubview:imgView];
        }
        [cell.evaluateScrollView setContentSize:CGSizeMake(imageArray.count * 120 - 10, 80)];

    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    NSMutableDictionary *evalDic;
    if ( tableView == tblFrontView)
        evalDic = (NSMutableDictionary *)(frontEvaluateArray[indexPath.row]);
    else
        evalDic = (NSMutableDictionary *)(backEvaluateArray[indexPath.row]);
    EvaluateDetailViewController *detailViewController = [[EvaluateDetailViewController alloc] initWithNibName:@"EvaluateDetailViewController" bundle:nil];
    detailViewController.dicEvalData = evalDic;
    [self.navigationController pushViewController:detailViewController animated:YES];

    
}


@end
