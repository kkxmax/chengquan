//
//  ReformViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ReformViewController.h"
#import "Global.h"

@interface ReformViewController ()

@end

@implementation ReformViewController
@synthesize editPictureView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.

    UIImage *img1 = [UIImage imageNamed:@"wo_jilu"];
    UIImage *img2 = [UIImage imageNamed:@"wo_renzheng"];

    NSMutableArray *aryPic = [[NSMutableArray alloc] init];
    [aryPic addObject:img1];
    [aryPic addObject:img2];
    
    if ( IS_IPHONE_5_OR_LESS )
        editPictureView = [[EditPictureView alloc] initWithFrame:CGRectMake(28, 395, 262, 84) :aryPic];
    else
        editPictureView = [[EditPictureView alloc] initWithFrame:CGRectMake(83, 395, 262, 84) :aryPic];
    
    [self.viewContent addSubview:editPictureView];
    
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

@end
