//
//  EvaluateDetailViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "EvaluateDetailViewController.h"
#import "EvaluateDetailTableViewCell.h"
#import "EvaluateMoreTableViewCell.h"
#import "UIImageView+WebCache.h"
#import "Global.h"

@interface EvaluateDetailViewController ()
{
    NSMutableArray *aryReplyData;
}
@end

@implementation EvaluateDetailViewController
@synthesize tblEvalView;
@synthesize dicEvalData;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    aryReplyData = [[NSMutableArray alloc] init];
    
    aryReplyData = dicEvalData[@"replys"];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
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
    
    return aryReplyData.count + 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.row == 0)
        return 285.f;
    else
        return 105.f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {

    UITableViewCell *cell = [[UITableViewCell alloc] init];
    
    if (indexPath.row == 0) {
        static NSString *simpleTableIdentifier = @"EvaluateDetailTableViewCell";
        EvaluateDetailTableViewCell *cell = (EvaluateDetailTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        
        if (cell == nil) {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"EvaluateDetailTableViewCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
        }
        
        NSString *logoPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dicEvalData[@"ownerLogo"]];
        [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:logoPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
        
        cell.lblTitle.text = dicEvalData[@"ownerRealname"];
        cell.lblEval.text = dicEvalData[@"kindName"];
        cell.lblContent.text = dicEvalData[@"content"];
        cell.lblMore.text = dicEvalData[@"content"];
        
        NSMutableArray *aryPath = dicEvalData[@"imgPaths"];
        
        if ( aryPath == nil || aryPath.count == 0  ) {
            cell.scrollThumb.hidden = YES;
        }
        else
        {
            for (int i = 0; i < aryPath.count; i++)
            {
                UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * 90, 0, 80, 80)];
                NSString *imgPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPath[i]];
                [imgView sd_setImageWithURL:[NSURL URLWithString:imgPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
                [cell.scrollThumb addSubview:imgView];
            }
            [cell.scrollThumb setContentSize:CGSizeMake(aryPath.count * 90 - 10, 80)];
        }

        return cell;
    }
    else
    {
        NSDictionary *dic = aryReplyData[indexPath.row - 1];
        static NSString *simpleTableIdentifier = @"EvaluateMoreTableViewCell";
        EvaluateMoreTableViewCell *cell = (EvaluateMoreTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        
        if (cell == nil) {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"EvaluateMoreTableViewCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
        }
        
        NSString *logoPath = [NSString stringWithFormat:@"%@%@", BASE_WEB_URL, dicEvalData[@"ownerLogo"]];
        [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:logoPath] placeholderImage:[UIImage imageNamed:@"bg_pic"]];
        cell.lblReply.text = dic[@"content"];
        NSString *strDate = dic[@"writeString"];
        strDate = [strDate substringToIndex:[strDate rangeOfString:@" "].location];
        cell.lblDate.text = strDate;
        
        return cell;
    }
    
    return cell;

}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
}

@end
