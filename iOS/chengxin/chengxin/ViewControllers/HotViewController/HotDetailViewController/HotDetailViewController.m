//
//  HotDetailViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HotDetailViewController.h"
#import "HotDetailMainCell.h"
#import "HotDetailPersonalCell.h"
#import "Global.h"

@interface HotDetailViewController ()
{
    NSMutableArray *aryPhoto;
}
@end

@implementation HotDetailViewController
@synthesize lblContent, contentHeight;
@synthesize scrollInfoView, tblPersonalView, tblOfficeView, tblHeight;
@synthesize btnPersonal, btnOffice, personalSeparator, officeSeparator;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    //lblContent = [[UILabel alloc] initWithFrame:CGRectMake(12, 20, SCREEN_WIDTH - 24, 500)];
    //lblContent.numberOfLines = 300;
    [lblContent setText:@"将暮未暮  ：   对数组操作中出现空指针，很多情况下是一些刚开始学习编程的朋友常犯的错误，即把数组的初始化和数组元素的初始化混淆起来了。数组的初始化是对数组分配需要的空间，而初始化后的数组，其中的元素并没有实例化，依然是空的，所以还需要"];
    //[lblContent setFont:FONT_16];
    //[lblContent setTextColor:BLACK_COLOR_51];
    [lblContent sizeToFit];
    
    contentHeight = lblContent.frame.size.height + 40;
    
    UIImage *img1 = [UIImage imageNamed:@"wo_jilu"];
    UIImage *img2 = [UIImage imageNamed:@"wo_renzheng"];
    UIImage *img3 = [UIImage imageNamed:@"1100"];
    
    aryPhoto = [[NSMutableArray alloc] init];
    [aryPhoto addObject:img1];
    [aryPhoto addObject:img2];
    [aryPhoto addObject:img3];
    
    tblHeight = scrollInfoView.frame.size.height;
    
    tblPersonalView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, tblHeight)];
    tblPersonalView.dataSource = self;
    tblPersonalView.delegate = self;
    [scrollInfoView addSubview:tblPersonalView];
    
    tblOfficeView = [[UITableView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH, 0, SCREEN_WIDTH, tblHeight)];
    tblOfficeView.dataSource = self;
    tblOfficeView.delegate = self;
    [scrollInfoView addSubview:tblOfficeView];
    
    [scrollInfoView setContentSize:CGSizeMake(SCREEN_WIDTH * 2, tblHeight)];
    scrollInfoView.delegate = self;
    [self onClickPersonalButton:nil];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    
    [tblPersonalView removeFromSuperview];
    [tblOfficeView removeFromSuperview];
    
    tblPersonalView = nil;
    tblOfficeView = nil;
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

#pragma UITableViewDelegate & UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return 3;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {

    return tblHeight;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [[UITableViewCell alloc] init];
    
    if ( tableView == tblPersonalView)
    {
        static NSString *simpleTableIdentifier = @"HotDetailPersonalCell";
        HotDetailPersonalCell *cell = (HotDetailPersonalCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        
        if (cell == nil) {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"HotDetailPersonalCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
            cell.backgroundColor = [UIColor clearColor];
        }
        
        for (int i = 0; i < 3; i++)
        {
            UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * 90, 0, 80, 80)];
            [imgView setImage:aryPhoto[i]];
            [cell.scrollThumb addSubview:imgView];
        }
        [cell.scrollThumb setContentSize:CGSizeMake(3 * 90 - 10, 80)];
        
        return cell;
    }
    else if ( tableView == tblOfficeView)
    {
        static NSString *simpleTableIdentifier = @"HotDetailPersonalCell";
        HotDetailPersonalCell *cell = (HotDetailPersonalCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        
        if (cell == nil) {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"HotDetailPersonalCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
            cell.backgroundColor = [UIColor clearColor];
        }
        
        for (int i = 0; i < 3; i++)
        {
            UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * 90, 0, 80, 80)];
            [imgView setImage:aryPhoto[i]];
            [cell.scrollThumb addSubview:imgView];
        }
        [cell.scrollThumb setContentSize:CGSizeMake(3 * 90 - 10, 80)];
        
        return cell;

    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
}

#pragma mark - UIScrollViewDelegate
- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate {
    CGFloat pageWidth = scrollView.frame.size.width;
    float fractionalPage = scrollView.contentOffset.x / pageWidth;
    NSInteger page = lround(fractionalPage);
    if (page == 0) {
        [btnPersonal setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
        [btnOffice setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
        personalSeparator.hidden = NO;
        officeSeparator.hidden = YES;
    }
    else if (page == 1)
    {
        [btnOffice setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
        [btnPersonal setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
        officeSeparator.hidden = NO;
        personalSeparator.hidden = YES;
    }

}


- (IBAction)onClickPersonalButton:(UIButton *)sender {
    [btnPersonal setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
    [btnOffice setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    personalSeparator.hidden = NO;
    officeSeparator.hidden = YES;

    [scrollInfoView setContentOffset:CGPointMake(0, 0)];
   
}

- (IBAction)onClickOfficeButton:(UIButton *)sender {
    [btnOffice setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
    [btnPersonal setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    officeSeparator.hidden = NO;
    personalSeparator.hidden = YES;
    
    [scrollInfoView setContentOffset:CGPointMake(SCREEN_WIDTH, 0)];
}

- (IBAction)onClickNavBackButton:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

@end
