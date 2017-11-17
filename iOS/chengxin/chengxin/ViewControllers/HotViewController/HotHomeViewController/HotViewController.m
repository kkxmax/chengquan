//
//  HotViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/24/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HotViewController.h"
#import "HotTableViewCell.h"
#import "HotDetailViewController.h"

@interface HotViewController ()
{
    NSMutableArray *aryPhoto;
}
@end

@implementation HotViewController
@synthesize tblHotView;
@synthesize messageNumberLabel;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    tblHotView.delegate = self;
    tblHotView.dataSource = self;
    // Customize message number label
    messageNumberLabel.layer.cornerRadius = messageNumberLabel.frame.size.width / 2;
    messageNumberLabel.layer.masksToBounds = YES;
    
    UIImage *img1 = [UIImage imageNamed:@"wo_jilu"];
    UIImage *img2 = [UIImage imageNamed:@"wo_renzheng"];
    UIImage *img3 = [UIImage imageNamed:@"1100"];

    aryPhoto = [[NSMutableArray alloc] init];
    [aryPhoto addObject:img1];
    [aryPhoto addObject:img2];
    [aryPhoto addObject:img3];

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
    return 5;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.row % 2) {
        return 249.0f;
    }
    else {
        return 169.0f;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *simpleTableIdentifier = @"HotTableViewCell";
    HotTableViewCell *cell = (HotTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"HotTableViewCell" owner:self options:nil];
        cell = [nib objectAtIndex:0];
        cell.backgroundColor = [UIColor clearColor];
    }
    
    if ( indexPath.row % 2 ) {

        for (int i = 0; i < 3; i++)
        {
            UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(i * 90, 0, 80, 80)];
            [imgView setImage:aryPhoto[i]];
            [cell.scrollThumb addSubview:imgView];
        }
        [cell.scrollThumb setContentSize:CGSizeMake(3 * 90 - 10, 80)];
    }

    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    HotDetailViewController *detailViewController = [[HotDetailViewController alloc] initWithNibName:@"HotDetailViewController" bundle:nil];

    // Push the view controller.
    [self.navigationController pushViewController:detailViewController animated:YES];
    
}

@end
