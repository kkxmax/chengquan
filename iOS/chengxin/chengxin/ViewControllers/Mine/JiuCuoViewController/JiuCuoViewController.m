//
//  JiuCuoViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "JiuCuoViewController.h"

@interface JiuCuoViewController ()
{
    NSMutableArray *aryPhoto;
}
@end

@implementation JiuCuoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [self getDataFromServer];
    
    for (int i = 0; i < 3; i++)
    {
        UIImageView *imgView = [[UIImageView alloc] initWithImage:aryPhoto[i]];
        [imgView setFrame:CGRectMake(i * 90, 0, 80, 80)];
        [self.scrollResonView addSubview:imgView];
    }
    
    [self.scrollResonView setContentSize:CGSizeMake(3 * 90 - 10, 80)];
    
    for (int i = 0; i < 3; i++)
    {
        UIImageView *imgView = [[UIImageView alloc] initWithImage:aryPhoto[i]];
        [imgView setFrame:CGRectMake(i * 90, 0, 80, 80)];
        [self.scrollPersonView addSubview:imgView];
    }
    
    [self.scrollPersonView setContentSize:CGSizeMake(3 * 90 - 10, 80)];
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

- (void)getDataFromServer {
    
    UIImage *img1 = [UIImage imageNamed:@"wo_jilu"];
    UIImage *img2 = [UIImage imageNamed:@"wo_renzheng"];
    UIImage *img3 = [UIImage imageNamed:@"1100"];
    
    aryPhoto = [[NSMutableArray alloc] init];
    [aryPhoto addObject:img1];
    [aryPhoto addObject:img2];
    [aryPhoto addObject:img3];
}

@end
