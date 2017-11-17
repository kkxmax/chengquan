//
//  WriteEvalViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "WriteEvalViewController.h"
#import "Global.h"

@interface WriteEvalViewController ()

@end

@implementation WriteEvalViewController
@synthesize btnFrontEval, btnBackEval, btnDetailEval, btnQuickEval;
@synthesize scrollPictureView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.

    UIImage *img1 = [UIImage imageNamed:@"wo_jilu"];
    UIImage *img2 = [UIImage imageNamed:@"wo_renzheng"];
    
    NSMutableArray *aryPic = [[NSMutableArray alloc] init];
    [aryPic addObject:img1];
    [aryPic addObject:img2];
    
    if ( IS_IPHONE_5_OR_LESS )
        scrollPictureView = [[ScrollPictureView alloc] initWithFrame:CGRectMake(28, 410, 262, 84) :aryPic];
    else
        scrollPictureView = [[ScrollPictureView alloc] initWithFrame:CGRectMake(83, 410, 262, 84) :aryPic];
    
    [self.viewContent addSubview:scrollPictureView];

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

- (IBAction)onClickFrontEval:(id)sender {
    [btnFrontEval setSelected:YES];
    [btnBackEval setSelected:NO];
}

- (IBAction)onClickBackEval:(id)sender {
    [btnFrontEval setSelected:NO];
    [btnBackEval setSelected:YES];
}

- (IBAction)onClickDetailEval:(id)sender {
    [btnDetailEval setSelected:YES];
    [btnQuickEval setSelected:NO];
}

- (IBAction)onClickQuickEval:(id)sender {
    [btnDetailEval setSelected:NO];
    [btnQuickEval setSelected:YES];
}

- (IBAction)onClickNavBackButton:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

@end
