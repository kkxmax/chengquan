//
//  MyJiuCuoViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "MyJiuCuoViewController.h"
#import "JiuCuoSuccessTableViewCell.h"
#import "JiuCuoViewController.h"
#import "Global.h"
#import "UIImageView+WebCache.h"

@interface MyJiuCuoViewController ()
{
   int tblHeight;
    NSMutableArray* aryErrorsInConsideration;
    NSMutableArray* aryErrorsSuccessed;
    NSMutableArray* aryErrorsFailed;
}
@end

enum {
    SELECT_DETAIL = 0,
    SELECT_SUCCESS,
    SELECT_FAIL
};

@implementation MyJiuCuoViewController
@synthesize scrollContentView, tblFailView, tblDetailView, tblSuccessView;
@synthesize btnFail, btnDetail, btnSuccess, sepFail, sepDetail, sepSuccess;
@synthesize selectType;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    aryErrorsInConsideration = [[NSMutableArray alloc] init];
    aryErrorsSuccessed = [[NSMutableArray alloc] init];
    aryErrorsFailed = [[NSMutableArray alloc] init];
    
    tblHeight = SCREEN_HEIGHT - NAVIGATION_HEIGHT - 40;
    scrollContentView.delegate = self;
    [scrollContentView setContentSize:CGSizeMake(SCREEN_WIDTH * 3, tblHeight)];
    
    
    [self onClickDetailButton:nil];
    
    [self getDataFromServer];
    
}
-(void)getDataFromServer
{
    [GeneralUtil showProgress];
    NSMutableDictionary* dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:ACTION_GETERRORLIST forKey:@"pAct"];
    [dicParams setObject:[NSNumber numberWithInt:0] forKey:@"status"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETERRORLIST Parameters:dicParams :^(NSObject *resObj) {
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        [GeneralUtil hideProgress];
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *aryData = dicRes[@"data"];
                
                [aryErrorsInConsideration removeAllObjects];
                [aryErrorsSuccessed removeAllObjects];
                [aryErrorsFailed removeAllObjects];
                
                for (int i = 0; i < aryData.count; i++) {
                    NSMutableDictionary *dic = [[NSMutableDictionary alloc] initWithDictionary:aryData[i]];
                    int status = [dic[@"status"] intValue];
                    if(status == 1)
                    {
                        [aryErrorsInConsideration addObject:dic];
                    }else if(status == 2)
                    {
                        [aryErrorsSuccessed addObject:dic];
                    }else if(status == 3)
                    {
                        [aryErrorsFailed addObject:dic];
                    }
                }
                if(selectType == SELECT_DETAIL)
                    [self.tblDetailView reloadData];
                else if(selectType == SELECT_SUCCESS)
                    [self.tblSuccessView reloadData];
                else if(selectType == SELECT_FAIL)
                    [self.tblFailView reloadData];
            }
        }
    }];
}

- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
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

- (void) selectItem:(NSInteger) type {
    [btnDetail setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    [btnSuccess setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    [btnFail setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    
    sepDetail.hidden = YES;
    sepSuccess.hidden = YES;
    sepFail.hidden = YES;
    
    if (type == SELECT_DETAIL) {
        [btnDetail setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
        sepDetail.hidden = NO;
    }
    else if (type == SELECT_SUCCESS) {
        [btnSuccess setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
        sepSuccess.hidden = NO;
    }
    else if (type == SELECT_FAIL) {
        [btnFail setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
        sepFail.hidden = NO;
    }

}

- (IBAction)onClickDetailButton:(id)sender {
    
    selectType = SELECT_DETAIL;
    [self selectItem:selectType];
    [self createDetailView];
    [scrollContentView setContentOffset:CGPointMake(0, 0)];
}

- (IBAction)onClickSuccessButton:(id)sender {
    selectType = SELECT_SUCCESS;
    [self selectItem:selectType];
    [self createSuccessView];
    [scrollContentView setContentOffset:CGPointMake(SCREEN_WIDTH, 0)];
}

- (IBAction)onClickFailButton:(id)sender {
    selectType = SELECT_FAIL;
    [self selectItem:selectType];
    [self createFailView];
    [scrollContentView setContentOffset:CGPointMake(SCREEN_WIDTH * 2, 0)];
}

- (void) createDetailView {
    if (tblDetailView == nil) {
        tblDetailView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, tblHeight)];
        [tblDetailView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
        tblDetailView.dataSource = self;
        tblDetailView.delegate = self;
        [scrollContentView addSubview:tblDetailView];
    }
}

- (void) createSuccessView {
    if (tblSuccessView == nil) {
        tblSuccessView = [[UITableView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH, 0, SCREEN_WIDTH, tblHeight)];
        [tblSuccessView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
        tblSuccessView.dataSource = self;
        tblSuccessView.delegate = self;
        [scrollContentView addSubview:tblSuccessView];
    }
}

- (void) createFailView {
    if (tblFailView == nil) {
        tblFailView = [[UITableView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH * 2, 0, SCREEN_WIDTH, tblHeight)];
        [tblFailView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
        tblFailView.dataSource = self;
        tblFailView.delegate = self;
        [scrollContentView addSubview:tblFailView];
    }
}

#pragma mark - UIScrollViewDelegate

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    if ( scrollView == scrollContentView ) {
        CGFloat pageWidth = scrollView.frame.size.width;
        float fractionalPage = scrollView.contentOffset.x / pageWidth;
        NSInteger page = lround(fractionalPage);
        
        if (page == SELECT_DETAIL) {
            selectType = SELECT_DETAIL;
            [self createDetailView];
        }
        else if (page == SELECT_SUCCESS)
        {
            selectType = SELECT_SUCCESS;
            [self createSuccessView];
        }
        else if (page == SELECT_FAIL)
        {
            selectType = SELECT_FAIL;
            [self createFailView];
        }
             
        [self selectItem:selectType];
    }
}

#pragma mark -  UITableViewDelegate & UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    if(tableView == tblDetailView)
    {
        return aryErrorsInConsideration.count;
    }else if(tableView == tblSuccessView)
    {
        return aryErrorsSuccessed.count;
    }else if(tableView == tblFailView)
    {
        return aryErrorsFailed.count;
    }
    return 10;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 116.f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [[UITableViewCell alloc] init];
    
    if ( tableView == tblDetailView)
    {
        static NSString *simpleTableIdentifier = @"JiuCuoSuccessTableViewCell";
        JiuCuoSuccessTableViewCell *cell = (JiuCuoSuccessTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        
        if (cell == nil) {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"JiuCuoSuccessTableViewCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
            cell.backgroundColor = [UIColor clearColor];
            NSMutableDictionary* dic = [aryErrorsInConsideration objectAtIndex:indexPath.row];
            cell.lblName.text = dic[@"estimaterName"];
            cell.lblCause.text = [NSString stringWithFormat:@"纠错依据: %@", dic[@"whyis"]];

            cell.lblTime.text = [GeneralUtil getDateHourMinFrom:dic[@"writeTimeString"]];
            cell.lblEstimationMarks.hidden = YES;
            int akind = [dic[@"estimaterAkind"] intValue];
            NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"estimaterLogo"] ]];
            [cell.imgPhoto sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:akind == 1 ? @"no_image_person1.png" : @"no_image_enter.png"]];
        }
        
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        return cell;
    
    }
    else if ( tableView == tblSuccessView )
    {
        static NSString *simpleTableIdentifier = @"JiuCuoSuccessTableViewCell";
        JiuCuoSuccessTableViewCell *cell = (JiuCuoSuccessTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        
        if (cell == nil) {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"JiuCuoSuccessTableViewCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
            cell.backgroundColor = [UIColor clearColor];
            
            NSMutableDictionary* dic = [aryErrorsSuccessed objectAtIndex:indexPath.row];
            cell.lblName.text = dic[@"estimaterName"];
            cell.lblCause.text = [NSString stringWithFormat:@"纠错依据: %@", dic[@"reason"]];
            cell.lblTime.text = [GeneralUtil getDateHourMinFrom:dic[@"writeTimeString"]];
            cell.lblEstimationMarks.hidden = NO;
            int akind = [dic[@"estimaterAkind"] intValue];
            NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"estimaterLogo"] ]];
            [cell.imgPhoto sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:akind == 1 ? @"no_image_person1.png" : @"no_image_enter.png"]];
        }
        
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        return cell;
    }
    else if ( tableView == tblFailView)
    {
        static NSString *simpleTableIdentifier = @"JiuCuoSuccessTableViewCell";
        JiuCuoSuccessTableViewCell *cell = (JiuCuoSuccessTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        
        if (cell == nil) {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"JiuCuoSuccessTableViewCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
            cell.backgroundColor = [UIColor clearColor];
            NSMutableDictionary* dic = [aryErrorsFailed objectAtIndex:indexPath.row];
            cell.lblName.text = dic[@"estimaterName"];
            cell.lblCause.text = [NSString stringWithFormat:@"纠错依据: %@", dic[@"reason"]];
            cell.lblTime.text = [GeneralUtil getDateHourMinFrom:dic[@"writeTimeString"]];
            cell.lblEstimationMarks.hidden = YES;
            NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dic[@"estimaterLogo"] ]];
            int akind = [dic[@"estimaterAkind"] intValue];
            [cell.imgPhoto sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:akind == 1 ? @"no_image_person1.png" : @"no_image_enter.png"]];
        }
        
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        return cell;
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    JiuCuoViewController *vc = [[JiuCuoViewController alloc] initWithNibName:@"JiuCuoViewController" bundle:nil];
    if(tableView == tblDetailView)
        vc.data = aryErrorsInConsideration[indexPath.row];
    else if(tableView == tblSuccessView)
        vc.data = aryErrorsSuccessed[indexPath.row];
    else if(tableView == tblFailView)
        vc.data = aryErrorsFailed[indexPath.row];
        
    [self.navigationController pushViewController:vc animated:YES];
}
@end
