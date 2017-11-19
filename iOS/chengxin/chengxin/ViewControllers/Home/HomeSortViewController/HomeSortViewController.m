//
//  HomeSortViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeSortViewController.h"
#import "Global.h"

@interface HomeSortViewController ()
{
    NSInteger nSelectedIndex;
}
@end

@implementation HomeSortViewController
@synthesize sortNameLabel, sortCheckImage;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    nSelectedIndex = [CommonData sharedInstance].sortOrderIndex - 1;
    [self updateTable];
}
- (void) DownloadNewsItem:(NSNotification *) notification
{
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)updateTable {
    for(int i = 0; i < sortNameLabel.count; i++) {
        if(i == nSelectedIndex) {
            [[sortNameLabel objectAtIndex:i] setTextColor:[UIColor blackColor]];
            [[sortCheckImage objectAtIndex:i] setHidden:NO];
        }else{
            [[sortNameLabel objectAtIndex:i] setTextColor:RGB_COLOR(205, 205, 205)];
            [[sortCheckImage objectAtIndex:i] setHidden:YES];
        }
    }
}

- (IBAction)setSelected:(id)sender {
    UIButton *button = (UIButton *)sender;
    nSelectedIndex = button.tag;
    [CommonData sharedInstance].sortOrderIndex = nSelectedIndex + 1;
    [self updateTable];
    [[NSNotificationCenter defaultCenter] postNotificationName:HIDE_SORT_VIEW_NOTIFICATION object:nil];
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
